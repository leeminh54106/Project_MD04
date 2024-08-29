package ra.project.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FormLogin {
    @NotBlank(message = "Tên người dùng không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
