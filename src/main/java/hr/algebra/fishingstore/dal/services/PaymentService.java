package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.PaymentRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Payment;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentDto.ResponseDto getById(Long id) {
        return mapToResponse(paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Not Found")));
    }

    public List<PaymentDto.ResponseDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentDto.ResponseDto create(PaymentDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        Payment payment = new Payment();
        payment.setAmount(order.getTotalPrice());
        payment.setCurrency(dto.getCurrency());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaypalTransactionId(dto.getPaypalTransactionId());
        payment.setOrder(order);

        return mapToResponse(paymentRepository.save(payment));
    }

    public PaymentDto.ResponseDto update(Long id, PaymentDto.EditDto dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Not Found"));

        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaypalTransactionId(dto.getPaypalTransactionId());
        if (dto.getPaymentStatus() == PaymentStatus.COMPLETED) {
            payment.setPaidAt(LocalDateTime.now());
        }

        return mapToResponse(paymentRepository.save(payment));
    }

    private PaymentDto.ResponseDto mapToResponse(Payment payment) {
        return new PaymentDto.ResponseDto(
                payment.getId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaidAt(),
                payment.getCreatedAt(),
                payment.getOrder().getId(),
                payment.getPaypalTransactionId()
        );
    }
}