package ra.project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.project.constants.RoleName;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
