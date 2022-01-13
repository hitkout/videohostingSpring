package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import ru.osminkin.springvideohosting.services.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Controller
public class MyPageController {
    private final MessageService messageService;
    private final UserService userService;
    private final PhotoService photoService;
    private final VideoService videoService;
    final TypeAndSortService typeAndSortService;
    private final PhotoRepository photoRepository;
    private final VideoRepository videoRepository;

    public MyPageController(MessageService messageService, UserService userService, PhotoService photoService, VideoService videoService, TypeAndSortService typeAndSortService, PhotoRepository photoRepository, VideoRepository videoRepository) {
        this.messageService = messageService;
        this.userService = userService;
        this.photoService = photoService;
        this.videoService = videoService;
        this.typeAndSortService = typeAndSortService;
        this.photoRepository = photoRepository;
        this.videoRepository = videoRepository;
    }

    @GetMapping("/auth/success/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId,
                             @RequestParam(value = "type", defaultValue = "videos") String type,
                             @RequestParam(value = "sort", defaultValue = "pop") String sort,
                             Authentication authentication,
                             Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", userService.findUserByEmail(authentication));
        typeAndSortService.getPage(type, sort, model, userId);
        return "channelMain";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "saveMessage")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelMessage(@PathVariable("userId") long userId,
                                     @ModelAttribute("messageFromForm") Message messageFromForm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        messageFromForm.setUser(userService.findUserById(userId));
        messageFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
        messageService.save(messageFromForm);
        return "redirect:/auth/success/channel/{userId}?type=records";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "deleteMessage")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannel(@PathVariable("userId") long userId,
                              @RequestParam("id") long id){
        messageService.deleteMessageById(id);
        return "redirect:/auth/success/channel/{userId}?type=records";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "savePhoto")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelPhoto(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("photoFromForm") Photo photoFromForm) throws IOException {
        photoService.savePhotoInDb(userId, file, photoFromForm);
        return "redirect:/auth/success/channel/{userId}?type=photos";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "deletePhoto")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String deletePhoto(@PathVariable("userId") long userId,
                              @RequestParam("id") long id){
        photoService.deletePhotoById(id);
        return "redirect:/auth/success/channel/{userId}?type=photos";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "saveVideo")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelVideo(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("videoFromForm") Video videoFromForm) throws IOException {
        videoService.saveVideoInDb(userId, file, videoFromForm);
        return "redirect:/auth/success/channel/{userId}?type=videos";
    }

    @PostMapping(value = "/auth/success/channel/{userId}", params = "deleteVideo")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String deleteVideo(@PathVariable("userId") long userId,
                              @RequestParam("id") long id){
        videoService.deleteVideoById(id);
        return "redirect:/auth/success/channel/{userId}?type=videos";
    }

    @GetMapping("/auth/success/channel/{userId}/videos")
    public String getChannelVideos(@PathVariable("userId") long userId, Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("videos", videoRepository.findAll());
        return "channelVideos";
    }

    @GetMapping("/auth/success/channel/{userId}/photos")
    public String getChannelPhoto(@PathVariable("userId") long userId, Model model){
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

//    @GetMapping("/channel/{userId}/photos/{photoId}")
//    public String getPhoto(@PathVariable("userId") long userId, @PathVariable("photoId") long photoId, Model model){
//        Photo photo = photoRepository.findById(photoId).orElseThrow(()->new IllegalArgumentException("Photo doesn't exists"));
//        model.addAttribute("user", userService.findUserById(userId));
//        model.addAttribute("photo", photo);
//        return "showPhoto";
//    }
}