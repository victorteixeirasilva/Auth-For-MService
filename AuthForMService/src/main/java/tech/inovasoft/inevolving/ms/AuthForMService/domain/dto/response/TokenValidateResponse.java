package tech.inovasoft.inevolving.ms.AuthForMService.domain.dto.response;

public record TokenValidateResponse(
        String quemVaiConsumir,
        String quemVaiSerConsumido
) {
}
