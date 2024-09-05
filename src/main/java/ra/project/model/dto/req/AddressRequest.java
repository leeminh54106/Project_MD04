package ra.project.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressRequest {
    private Long id;
    @NotBlank(message = "địa chỉ không được để trống")
    private String fullAddress;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    @NotBlank(message = "tên người nhận không được để trống")
    private String receiveName;
    private Long userId;
}