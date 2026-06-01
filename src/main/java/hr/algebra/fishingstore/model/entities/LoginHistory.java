package hr.algebra.fishingstore.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_historys")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private boolean success;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime loginAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
