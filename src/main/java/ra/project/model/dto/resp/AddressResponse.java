package ra.project.model.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class AddressResponse {
    private Long id;
    private String fullAddress;
    private String phone;
    private String receiveName;
}
