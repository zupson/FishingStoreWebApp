package hr.algebra.fishingstore.model.entities;

import hr.algebra.fishingstore.model.enums.Currency;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @UpdateTimestamp
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column( updatable = false)
    private Long paypalId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}