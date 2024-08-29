package ra.project.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String accessToken;
    private final String type = "Bearer";
    private String fullName;
    private String username;
    private String email;
    private String avatar;
    private String phone;
    private Boolean status;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedAt;
    private Boolean isDeleted;
    private Set<String> roles;
}
