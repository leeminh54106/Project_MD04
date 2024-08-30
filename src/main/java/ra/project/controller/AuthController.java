package ra.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.FormLogin;
import ra.project.model.dto.req.FormRegister;
import ra.project.service.IAuthService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@Valid @RequestBody FormRegister formRegister) throws CustomException {
        authService.register(formRegister);
        return ResponseEntity.created(URI.create("api/v1/auth/register")).body("Register successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody FormLogin formLogin) throws CustomException
    {
        return new ResponseEntity<>(authService.login(formLogin), HttpStatus.OK);
    }
}
