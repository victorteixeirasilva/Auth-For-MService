package tech.inovasoft.inevolving.ms.AuthForMService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.inovasoft.inevolving.ms.AuthForMService.domain.model.MicroService;
import tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces.MicroServiceRepositoryJPA;

@Service
public class AuthService {

    @Autowired
    private MicroServiceRepositoryJPA microServiceRepositoryJPA;

    public MicroService findByName(String microServiceName) {
        var microServiceOptional = microServiceRepositoryJPA.findByName(microServiceName.toLowerCase());
        MicroService microService;

        if (microServiceOptional.isEmpty()) {
            throw new IllegalArgumentException("Microservice not found");
        } else {
            return microServiceOptional.get();
        }
    }

    public MicroService save(MicroService newUser) {
        return microServiceRepositoryJPA.save(newUser);
    }


}
