package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.PaymentDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.PaymentRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentDto.ResponseDto getById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment Not Found"));
        return mapToResponse(payment);
    }

    public List<PaymentDto.ResponseDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PaymentDto.ResponseDto create(PaymentDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        Payment payment = new Payment();
        payment.setAmount(dto.amount());
        payment.setCurrency(dto.currency());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setPaymentStatus(dto.paymentStatus());
        payment.setPaypalId(dto.paypalId());
        payment.setOrder(order);

        Payment createdPayment = paymentRepository.save(payment);
        return mapToResponse(createdPayment);
    }

    public PaymentDto.ResponseDto update(Long id, PaymentDto.EditDto dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Not Found"));

        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        payment.setAmount(dto.amount());
        payment.setCurrency(dto.currency());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setPaymentStatus(dto.paymentStatus());
        payment.setPaypalId(dto.paypalId());
        payment.setOrder(order);

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToResponse(updatedPayment);
    }

    public boolean delete(Long id) {
        if (!paymentRepository.existsById(id)) return false;

        paymentRepository.deleteById(id);
        return true;
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
                payment.getPaypalId(),
                payment.getOrder().getId()
        );
    }
}
