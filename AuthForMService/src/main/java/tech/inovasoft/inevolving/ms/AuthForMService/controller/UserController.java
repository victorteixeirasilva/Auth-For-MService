package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "Usuário")
@RestController
@RequestMapping("/auth/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @GetMapping
    public ResponseEntity<String> createUserAuth() {
        return ResponseEntity.ok("User created Auth");
    }
}
