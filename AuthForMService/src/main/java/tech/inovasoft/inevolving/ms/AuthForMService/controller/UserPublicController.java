package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Tag(name = "Usuário | User")
@RestController
@RequestMapping("/api/user")
public class UserPublicController {

    @GetMapping
    public ResponseEntity<String> createUser() {
        return ResponseEntity.ok("User created");
    }
}
