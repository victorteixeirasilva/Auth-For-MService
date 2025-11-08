package tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;

import java.util.UUID;

public interface MicroServiceRepositoryJPA extends JpaRepository<MicroService, UUID> {
    UserDetails findByMicroServiceName(String microServiceName);
}
