package ra.project.model.dto.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    private Long userId;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private String note;
}
