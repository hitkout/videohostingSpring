package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import ru.osminkin.springvideohosting.services.*;

import java.io.IOException;

@Controller
public class MyPageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private VideoService videoService;

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/auth/success/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId, Model model){
        model.addAttribute("messages", messageService.findAllUserMessages(userId));
        model.addAttribute("user", userService.findUserById(userId));
        return "channelMain";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "save")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postSuccessPage(@PathVariable("userId") long userId,
                                  @RequestParam("file") MultipartFile file,
                                  @ModelAttribute("messageMessage") Message messageMessage) throws IOException {
        //messageService.setMessageFromForm(userService.findUserById(userId), file, messageMessage);
        messageService.save(messageMessage);
        return "redirect:/auth/success/channel/{userId}";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "delete")
    public String postChannel(@RequestParam("id") long id){
        messageService.deleteMessageById(id);
        return "redirect:/auth/success/channel/{userId}";
    }

    @GetMapping("/auth/success/channel/{userId}/videos")
    public String getChannelVideos(@PathVariable("userId") long userId, Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("videos", videoRepository.findAll());
        return "channelVideos";
    }

    @PostMapping("/auth/success/channel/{userId}/videos")
    public String postChannelVideo(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("video") Video videoFromForm) throws IOException {
        videoService.saveVideoInDb(userId, file, videoFromForm);
        return "redirect:/auth/success/channel/{userId}/videos";
    }

    @GetMapping("/auth/success/channel/{userId}/photos")
    public String getChannelPhoto(@PathVariable("userId") long userId, Model model){
        //Iterable<Photo> photos = photoRepository.findPhotosByUserId(user.getId());
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("photos", photoRepository.findLast10());
        model.addAttribute("lastElement", photoRepository.getLastId());
        return "channelPhoto";
    }

    @GetMapping("/auth/success/channel/{userId}/photos/{idPhoto}")
    @ResponseBody
    public Iterable<Photo> loadPhoto(@PathVariable("userId") Long userId,
                                     @PathVariable("idPhoto") Long idPhoto){
        while (photoRepository.findById(idPhoto).isEmpty())
            idPhoto--;
        return photoRepository.findAllPhotosByIdBetweenOrderByIdDesc(idPhoto-2L, idPhoto);
    }

    @PostMapping("/auth/success/channel/{userId}/photos")
    public String postChannelPhoto(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("photo") Photo photoFromForm) throws IOException {
        photoService.savePhotoInDb(userId, file, photoFromForm);
        return "redirect:/auth/success/channel/{userId}/photos";
    }

//    @GetMapping("/channel/{userId}/photos/{photoId}")
//    public String getPhoto(@PathVariable("userId") long userId, @PathVariable("photoId") long photoId, Model model){
//        Photo photo = photoRepository.findById(photoId).orElseThrow(()->new IllegalArgumentException("Photo doesn't exists"));
//        model.addAttribute("user", userService.findUserById(userId));
//        model.addAttribute("photo", photo);
//        return "showPhoto";
//    }
}