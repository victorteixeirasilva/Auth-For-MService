package tech.inovasoft.inevolving.ms.AuthForMService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.inovasoft.inevolving.ms.AuthForMService.repository.interfaces.MicroServiceRepositoryJPA;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    MicroServiceRepositoryJPA repositoryJPA;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repositoryJPA.findByMicroServiceName(username);
    }
}
