package ra.project.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.project.constants.EHttpStatus;
import ra.project.exception.CustomException;
import ra.project.model.dto.resp.ErrorResponse;
import ra.project.model.dto.resp.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse<String>> handleNoSuchElelement(NoSuchElementException ex) {
        return new ResponseEntity<>(new ErrorResponse<>("Error", ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < ex.getAllErrors().size(); i++) {
            map.put("error: " + (i + 1), ex.getAllErrors().get(i).getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse<>("Error", map, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> handleSuccessException(SuccessException ex) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(ex.getMessage())
                        .build(),
                HttpStatus.OK);
    }
}
