package ra.project.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100,unique=true)
    private String sku;
    @Column(length = 100)
    private String name;
    private String description;
    @Column( columnDefinition = "Decimal(10,2)")
    private Double price;
    private Integer quantity;
    private String image;
    private Date createdAt;
    private Date updatedAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Categories categories;
}
