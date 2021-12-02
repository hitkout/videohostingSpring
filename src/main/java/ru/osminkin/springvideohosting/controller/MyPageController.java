package ru.osminkin.springvideohosting.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.MessageRepository;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import ru.osminkin.springvideohosting.repository.UserRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import ru.osminkin.springvideohosting.services.AuthenticatedUserService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/auth/success")
public class MyPageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId, Model model, User user){
        user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        Iterable<Message> messages = messageRepository.findMessagesByUserId(user.getId());
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        return "channelMain";
    }

    @PostMapping(value = "/channel/{userId}", params = "save")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postSuccessPage(@PathVariable("userId") long userId,
                                  @RequestParam("file") MultipartFile file,
                                  @ModelAttribute("messageMessage") Message messageMessage,
                                  Model model) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPath);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            messageMessage.setFilename(resultFilename);
        }
        messageMessage.setUser(user);
        messageRepository.save(messageMessage);
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        model.addAttribute(user);
        return "redirect:/auth/success/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "delete")
    public String postChannel(@RequestParam("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/auth/success/channel/{userId}";
    }

    @GetMapping("/channel/{userId}/videos")
    public String getChannelVideos(@PathVariable("userId") long userId, Model model){
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        Iterable<Video> videos = videoRepository.findVideosById(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("videos", videos);
        return "channelVideos";
    }

    @GetMapping("/channel/{userId}/photos")
    public String getChannelPhoto(@PathVariable("userId") long userId, Model model){
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        Iterable<Photo> photos = photoRepository.findPhotosByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("photos", photos);
        return "channelPhoto";
    }

    @PostMapping("/channel/{userId}/photos")
    public String postChannelPhoto(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("photo") Photo photoFromForm,
                                   Model model) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPath);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            photoFromForm.setFilename(resultFilename);
        }
        photoFromForm.setUser(user);
        photoRepository.save(photoFromForm);
        Iterable<Photo> photos = photoRepository.findAll();
        model.addAttribute("photos", photos);
        model.addAttribute(user);
        return "channelPhoto";
    }

    @GetMapping("/channel/{userId}/photos/{photoId}")
    public String getPhoto(@PathVariable("userId") long userId, @PathVariable("photoId") long photoId, Model model){
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        Photo photo = photoRepository.findById(photoId).orElseThrow(()->new IllegalArgumentException("Photo doesn't exists"));
        model.addAttribute("user", user);
        model.addAttribute("photo", photo);
        return "showPhoto";
    }

    @GetMapping("/channel/{userId}/about")
    public String getChannelAbout(@PathVariable("userId") long userId, Model model){
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        model.addAttribute("user", user);
        return "channelAbout";
    }
}