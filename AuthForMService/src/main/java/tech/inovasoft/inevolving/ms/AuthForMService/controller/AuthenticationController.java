package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.request.AuthenticationRequest;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.LoginResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.MessageResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.UserRole;
import tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces.MicroServiceRepositoryJPA;
import tech.inovasoft.inevolving.ms.AuthForMService.service.TokenService;

import java.sql.Date;
import java.time.LocalDate;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/ms/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MicroServiceRepositoryJPA userRepositoryJPA;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid AuthenticationRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((MicroService) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid AuthenticationRequest data) {
        if (this.userRepositoryJPA.findByMicroServiceName(data.email().toLowerCase()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        MicroService newUser = new MicroService();
        newUser.setMicroserviceName(data.email().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setLastLogin(Date.valueOf(LocalDate.now()));
        newUser.setRole(UserRole.USER);

        this.userRepositoryJPA.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User created"));
    }

}
