package tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.request;

public record AuthenticationRequest(String microServiceName, String superSecret) {
}
