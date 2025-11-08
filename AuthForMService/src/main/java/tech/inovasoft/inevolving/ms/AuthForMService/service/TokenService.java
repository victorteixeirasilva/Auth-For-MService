package tech.inovasoft.inevolving.ms.AuthForMService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response.TokenValidateResponse;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final RSAPrivateKey privateKey;

    private final RSAPublicKey publicKey;

    public TokenService(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String generateToken(MicroService microService, String microServiceNameReceiver) {
        try {
            return JWT.create()
                    .withIssuer("AuthForMService")
                    .withSubject(microService.getName())
                    .withAudience(microServiceNameReceiver)
                    .withExpiresAt(createExpirationDate())
                    .sign(Algorithm.RSA256(null, privateKey));
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while generating token", e);
        }
    }

    public TokenValidateResponse validateToken(String token) {
        try {

            Algorithm algorithm = Algorithm.RSA256(publicKey);
            return new TokenValidateResponse(
                    JWT.require(algorithm)
                            .withIssuer("AuthForMService")
                            .build()
                            .verify(token)
                            .getSubject(),
                    JWT.require(algorithm)
                            .withIssuer("AuthForMService")
                            .build()
                            .verify(token)
                            .getAudience().getFirst()
            );
        } catch (IncorrectClaimException e) {
            throw new RuntimeException("Invalid token");
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant createExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }







}
