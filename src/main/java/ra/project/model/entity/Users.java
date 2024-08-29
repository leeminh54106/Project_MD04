package ra.project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, unique = true)
    private String username;
    @Column(length = 100, unique = true)
    private String email;
    @Column(length = 100)
    private String fullName;
    private Boolean status;
    @Column(length = 255)
    private String password;
    @Column(length = 255)
    private String avatar;
    @Column( unique = true, length = 15)
    private String phone;
    @Column(length = 254)
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;
}
