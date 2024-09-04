package ra.project.model.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private String name;
}
