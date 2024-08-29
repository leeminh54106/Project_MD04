package ra.project.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//400,404
public class ErrorResponse<T> {
    private String message;
    private T content;
    private HttpStatus httpStatus;
}
