package hr.algebra.fishingstore.model.entities;

import hr.algebra.fishingstore.model.enums.Currency;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private String paypalTransactionId;
}