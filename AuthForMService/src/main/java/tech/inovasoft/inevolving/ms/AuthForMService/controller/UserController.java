package tech.inovasoft.inevolving.ms.AuthForMService.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;
import tech.inovasoft.inevolving.ms.AuthForMService.service.TokenService;

@Tag(name = "Usuário")
@RestController
@RequestMapping("/auth/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<String> createUserAuth(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "
//            return ResponseEntity.ok("Token sem Bearer: " + token);

            var validateToken = tokenService.validateToken(token);
            return ResponseEntity.ok(validateToken.quemVaiConsumir() + "/" + validateToken.quemVaiSerConsumido());

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não encontrado");
    }
}
