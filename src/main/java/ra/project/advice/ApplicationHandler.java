package ra.project.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ra.project.exception.CustomException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ApplicationHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e)
    {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }


}
