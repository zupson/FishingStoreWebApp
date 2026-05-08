package hr.algebra.fishingstore.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
