package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import ru.osminkin.springvideohosting.services.MessageService;
import ru.osminkin.springvideohosting.services.UserService;
import java.io.IOException;

@Controller
@RequestMapping("/auth/success")
public class MyPageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId, Model model){
        model.addAttribute("messages", messageService.findAllUserMessages(userId));
        model.addAttribute("user", userService.findUserById(userId));
        return "channelMain";
    }

    @PostMapping(value = "/channel/{userId}", params = "save")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postSuccessPage(@PathVariable("userId") long userId,
                                  @RequestParam("file") MultipartFile file,
                                  @ModelAttribute("messageMessage") Message messageMessage) throws IOException {
        messageService.setMessageFromForm(userService.findUserById(userId), file, messageMessage);
        messageService.save(messageMessage);
        return "redirect:/auth/success/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "delete")
    public String postChannel(@RequestParam("id") long id){
        messageService.deleteMessageById(id);
        return "redirect:/auth/success/channel/{userId}";
    }

//    @GetMapping("/channel/{userId}/videos")
//    public String getChannelVideos(@PathVariable("userId") long userId, Model model){
//        //Iterable<Video> videos = videoRepository.findVideosById(user.getId());
//        model.addAttribute("user", userService.findUserById(userId));
//        //model.addAttribute("videos", videos);
//        return "channelVideos";
//    }

//    @GetMapping("/channel/{userId}/photos")
//    public String getChannelPhoto(@PathVariable("userId") long userId, Model model){
//        //Iterable<Photo> photos = photoRepository.findPhotosByUserId(user.getId());
//        model.addAttribute("user", userService.findUserById(userId));
//        //model.addAttribute("photos", photos);
//        return "channelPhoto";
//    }

//    @PostMapping("/channel/{userId}/photos")
//    public String postChannelPhoto(@PathVariable("userId") long userId,
//                                   @RequestParam("file") MultipartFile file,
//                                   @ModelAttribute("photo") Photo photoFromForm,
//                                   Model model) throws IOException {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
//        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
//            File upload = new File(uploadPath);
//            if (!upload.exists()){
//                upload.mkdir();
//            }
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFilename = uuidFile + "." + file.getOriginalFilename();
//            file.transferTo(new File(uploadPath + "/" + resultFilename));
//            photoFromForm.setFilename(resultFilename);
//        }
//        //photoFromForm.setUser(user);
//        photoRepository.save(photoFromForm);
//        Iterable<Photo> photos = photoRepository.findAll();
//        model.addAttribute("photos", photos);
//        model.addAttribute(user);
//        return "channelPhoto";
//    }

//    @GetMapping("/channel/{userId}/photos/{photoId}")
//    public String getPhoto(@PathVariable("userId") long userId, @PathVariable("photoId") long photoId, Model model){
//        Photo photo = photoRepository.findById(photoId).orElseThrow(()->new IllegalArgumentException("Photo doesn't exists"));
//        model.addAttribute("user", userService.findUserById(userId));
//        model.addAttribute("photo", photo);
//        return "showPhoto";
//    }

//    @GetMapping("/channel/{userId}/about")
//    public String getChannelAbout(@PathVariable("userId") long userId, Model model){
//        model.addAttribute("user", userService.findUserById(userId));
//        return "channelAbout";
//    }
}