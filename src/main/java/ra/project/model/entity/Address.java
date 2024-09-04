package ra.project.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String fullAddress;

    @Column(length = 15)
    private String phone;

    @Column(length = 50)
    private String receiveName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
