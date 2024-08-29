package ra.project.model.dto.req;

import io.grpc.stub.ServerCalls;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FormRegister {
    @NotBlank(message = "tên tài khoản không được để trống!")
    private String username;
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;


}
