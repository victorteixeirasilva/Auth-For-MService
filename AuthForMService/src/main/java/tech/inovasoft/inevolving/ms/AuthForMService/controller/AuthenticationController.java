package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.request.AuthenticationRequest;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.LoginResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.MessageResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;
import tech.inovasoft.inevolving.ms.AuthForMService.service.AuthService;
import tech.inovasoft.inevolving.ms.AuthForMService.service.TokenService;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/auth/ms/authentication")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login/{microServiceNameReceiver}")
    public ResponseEntity<LoginResponse> login(@PathVariable String microServiceNameReceiver, @RequestBody AuthenticationRequest data) {
        var microService = authService.findByName(data.microServiceName().toLowerCase());
        validatePassword(data.superSecret(), microService.getSuperSecret());
        var token = tokenService.generateToken(microService, microServiceNameReceiver);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid AuthenticationRequest data, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "

            var validateToken = tokenService.validateToken(token);

            if (validateToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token inválido"));
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token não encontrado"));
        }


        try {
            authService.findByName(data.microServiceName().toLowerCase());
        } catch (Exception e) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.superSecret());
            MicroService newUser = new MicroService();
            newUser.setName(data.microServiceName().toLowerCase());
            newUser.setSuperSecret(encryptedPassword);

            try {
                authService.save(newUser);
                return ResponseEntity.ok(new MessageResponse("User created"));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!new BCryptPasswordEncoder().matches(password, confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

}
