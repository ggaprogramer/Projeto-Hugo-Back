package projeto.hugo.terapia.authentication.controller;

import projeto.hugo.terapia.authentication.dto.*;
import projeto.hugo.terapia.authentication.security.TokenService;
import projeto.hugo.terapia.authentication.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response){
        return authService.login(loginDTO, response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDTO> register(@RequestBody RegistroDTO registroDTODTO){
        return authService.registro(registroDTODTO);
    }

    @PostMapping("/is-authenticated")
    public ResponseEntity<isAuthenticatedResponseDTO> isAuthenticated(@RequestBody isAuthenticatedDTO isAuthenticatedDTO) {
        return authService.isAuthenticated(isAuthenticatedDTO);
    }

}
