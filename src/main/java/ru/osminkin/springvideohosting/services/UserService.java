package ru.osminkin.springvideohosting.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.controller.ResourceNotFoundException;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${upload.path.image}")
    private String uploadPathImages;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<User> findBySearch(String search){
        return userRepository.findBySearch(search);
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public User findUserByAuthentication(Authentication authentication){
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void banUserById(Long bannedUser){
        userRepository.banUserById(bannedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void unbanUserById(Long bannedUser){
        userRepository.unbanUserById(bannedUser);
    }

    public String getUserStatus(Long userId){
        return userRepository.getUserStatus(userId);
    }

    public void saveUserPhoto(Long userId, MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathImages);
            if (!upload.exists()){
                if (upload.mkdir()){
                    log.info("Dir {} created", upload.getName());
                }
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPathImages + "/" + resultFilename));
            userRepository.updatePhoto(userId, resultFilename);
        }
    }

    public void saveUserPhoto(Long userId, String photoName){
        userRepository.updatePhoto(userId, photoName);
    }

    public void deleteUserPhoto(String photoName){
        File file = new File(uploadPathImages + "/" + photoName);
        if (file.delete()){
            log.info("Photo {} deleted", file.getName());
        }
    }

    public void subscribe(User follower, User followUser){
        userRepository.subscribe(follower, followUser);
    }

    public void unsubscribe(User follower, User followUser){
        userRepository.unsubscribe(follower, followUser);
    }

    public boolean isSubscribe(User follower, User followUser){
        return userRepository.isSubscribe(follower, followUser);
    }

    public List<User> findBySearchUserAuthFromSubscriptions(User follower, String search){
        return userRepository.findBySearchUserAuthFromSubscriptions(follower, search);
    }

    public List<User> findAllUserByUserAuthFromSubscriptions(User follower){
        return userRepository.findAllUserByUserAuthFromSubscriptions(follower);
    }

    public void changeName(Long id, String name){
        User user = userRepository.getById(id);
        user.setFirstName(name);
        userRepository.save(user);
    }

    public void changeSurname(Long id, String surname){
        User user = userRepository.getById(id);
        user.setFirstName(surname);
        userRepository.save(user);
    }

    public boolean changeUserPassword(Long id, String oldPassword, String password){
        if (passwordEncoder.matches(oldPassword, findUserById(id).getPassword())){
            userRepository.changeUserPassword(id, passwordEncoder.encode(password));
            return true;
        }
        return false;
    }

    public Long getSubscribersCount(User user){
        return userRepository.getSubscribersCount(user);
    }
}
