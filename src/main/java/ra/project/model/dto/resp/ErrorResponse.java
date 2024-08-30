package ra.project.model.dto.resp;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//400,404
public class ErrorResponse<T> {
    private String message;
    private T content;
    private HttpStatus httpStatus;
}
