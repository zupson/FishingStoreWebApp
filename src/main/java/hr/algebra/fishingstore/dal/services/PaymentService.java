package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.PaymentRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Payment;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String PAYMENT_NOT_FOUND = "Payment Not Found";
    public static final String ORDER_NOT_FOUND = "Order Not Found";
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public PaymentDto.ResponseDto getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PAYMENT_NOT_FOUND));
        return modelMapper.map(payment, PaymentDto.ResponseDto.class);
    }

    public List<PaymentDto.ResponseDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PaymentDto.ResponseDto.class))
                .toList();
    }

    public PaymentDto.ResponseDto create(PaymentDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));

        Payment payment = new Payment();
        payment.setAmount(order.getTotalPrice());
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCurrency(dto.getCurrency());
        payment.setPaymentMethod(dto.getPaymentMethod());

        return modelMapper.map(paymentRepository.save(payment), PaymentDto.ResponseDto.class);
    }

    public PaymentDto.ResponseDto update(Long id, PaymentDto.EditDto dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PAYMENT_NOT_FOUND));

        modelMapper.map(dto, payment);

        if (dto.getPaymentStatus() == PaymentStatus.PAID)
            payment.setPaidAt(LocalDateTime.now());

        return modelMapper.map(paymentRepository.save(payment), PaymentDto.ResponseDto.class);
    }
}