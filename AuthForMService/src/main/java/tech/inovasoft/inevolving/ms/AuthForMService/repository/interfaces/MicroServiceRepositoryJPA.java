package tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MicroServiceRepositoryJPA extends JpaRepository<MicroService, UUID> {
    Optional<MicroService> findByName(String microServiceName);
}
