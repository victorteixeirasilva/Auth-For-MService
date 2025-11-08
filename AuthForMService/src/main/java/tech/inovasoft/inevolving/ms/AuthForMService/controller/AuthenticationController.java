package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.request.AuthenticationRequest;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.LoginResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.MessageResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;
import tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces.MicroServiceRepositoryJPA;
import tech.inovasoft.inevolving.ms.AuthForMService.service.TokenService;

import java.sql.Date;
import java.time.LocalDate;

@Tag(name = "Autenticação")
@RestController
@RequestMapping("/ms/authentication")
public class AuthenticationController {

    @Autowired
    private MicroServiceRepositoryJPA microServiceRepositoryJPA;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login/{microServiceNameReceiver}")
    public ResponseEntity<LoginResponse> login(@PathVariable String microServiceNameReceiver, @RequestBody AuthenticationRequest data) {
        var ms = microServiceRepositoryJPA.findByName(data.email().toLowerCase()).orElseThrow();

        if (!new BCryptPasswordEncoder().matches(data.password(), ms.getSuperSecret())) {
            return ResponseEntity.badRequest().build();
        }

        var token = tokenService.generateToken(ms, microServiceNameReceiver);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Valid AuthenticationRequest data) {
        if (this.microServiceRepositoryJPA.findByName(data.email().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        MicroService newUser = new MicroService();
        newUser.setName(data.email().toLowerCase());
        newUser.setSuperSecret(encryptedPassword);

        this.microServiceRepositoryJPA.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User created"));
    }

}
