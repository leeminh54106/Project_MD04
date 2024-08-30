package ra.project.model.dto.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShoppingCartRequest {
    private Long productId;
    private Integer quantity;
}
