package ra.project.model.dto.req;

import lombok.*;
import ra.project.constants.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UDOrderStatusReq {
    private OrderStatus status;
}
