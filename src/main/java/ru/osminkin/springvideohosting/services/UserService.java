package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Value("${upload.path.image}")
    private String uploadPathImages;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<User> findBySearch(String search){
        return userRepository.findBySearch(search);
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    public User findUserByAuthentication(Authentication authentication){
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    public String getUserRole(User authUser){
        return userRepository.getUserRole(authUser);
    }

    public void banUserById(User authUser, Long bannedUser){
        if (Objects.equals(userRepository.getUserRole(authUser), "ADMIN"))
            userRepository.banUserById(bannedUser);
    }

    public void unbanUserById(User authUser, Long bannedUser){
        if (Objects.equals(userRepository.getUserRole(authUser), "ADMIN"))
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
                    System.out.println("Created directory");
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
            System.out.println("Photo deleted");
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
        userRepository.changeName(id, name);
    }

    public void changeSurname(Long id, String surname){
        userRepository.changeSurname(id, surname);
    }

    public void changeUserPassword(Long id, String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        userRepository.changeUserPassword(id, passwordEncoder.encode(password));
    }

    public Long getSubscribersCount(User user){
        return userRepository.getSubscribersCount(user);
    }
}
