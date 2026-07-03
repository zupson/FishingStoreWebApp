package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.PaymentRepository;
import hr.algebra.fishingstore.dal.services.payment.PayPalService;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Payment;
import hr.algebra.fishingstore.model.enums.OrderStatus;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import hr.algebra.fishingstore.session.CartSession;
import hr.algebra.fishingstore.utilities.PathConst;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;

import static hr.algebra.fishingstore.dal.services.PaymentService.PAYMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PayPalOrderService {
    private static final String ORDER_ID_Q_PARAM = "?orderId=";
    private static final String SUCCESS_URL = PathConst.MVC + PathConst.ORDERS + "/paypal/success" + ORDER_ID_Q_PARAM;
    private static final String CANCEL_URL = PathConst.MVC + PathConst.ORDERS + "/paypal/cancel";
    private static final String PAL_ORDER_CREATION_FAILED = "PayPal order creation failed";
    @Value("${app.base-url}")
    private String baseUrl;

    private final PayPalService payPalService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartSession cartSession;
    private final ModelMapper modelMapper;

    public String initPayPalPayment(Order createdOrder) {
        try {
            return payPalService.createOrder(
                    createdOrder.getTotalPrice(),
                    createdOrder.getId(),
                    baseUrl + SUCCESS_URL + createdOrder.getId(),
                    baseUrl + CANCEL_URL+ ORDER_ID_Q_PARAM +createdOrder.getId()
            );
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(PAL_ORDER_CREATION_FAILED, e);
        }
    }


    public OrderDto.ResponseDto confirmPayPalPayment(String payPalOrderId, Long orderId) {
        try {
            boolean isSuccessCaptured = payPalService.captureOrder(payPalOrderId);
            if (isSuccessCaptured) {
                Payment payment = getPayment(orderId);
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setPaypalTransactionId(payPalOrderId);
                payment.setPaidAt(LocalDateTime.now(Clock.systemUTC()));
                paymentRepository.save(payment);

                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new EntityNotFoundException(OrderService.ORDER_NOT_FOUND));
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);

                cartSession.clear();
            }
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException(OrderService.ORDER_NOT_FOUND));

            return modelMapper.map(order, OrderDto.ResponseDto.class);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private Payment getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(PAYMENT_NOT_FOUND));
    }
}