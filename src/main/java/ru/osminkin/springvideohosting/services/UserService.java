package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    public User findUserByEmail(Authentication authentication){
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }

    public void saveUserPhoto(long userId, MultipartFile file) throws IOException {
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

    public List<User> findAllUserByUserAuthFromSubscriptions(User follower){
        return userRepository.findAllUserByUserAuthFromSubscriptions(follower);
    }
}
