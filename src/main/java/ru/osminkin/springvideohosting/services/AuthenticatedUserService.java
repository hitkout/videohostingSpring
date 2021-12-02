package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

@Service
public class AuthenticatedUserService {
    @Autowired
    private UserRepository userRepository;

    public boolean hasId(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName).orElseThrow(()->new UsernameNotFoundException(""));
        return user.getId().equals(id);

    }
}
