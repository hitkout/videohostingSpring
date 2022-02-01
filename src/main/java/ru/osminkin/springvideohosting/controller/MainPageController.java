package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.services.*;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Controller
public class MainPageController {
    private final UserService userService;
    private final TypeAndSortService typeAndSortService;
    private final VideoService videoService;
    private final PhotoService photoService;

    public MainPageController(UserService userService, TypeAndSortService typeAndSortService, VideoService videoService, PhotoService photoService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
        this.videoService = videoService;
        this.photoService = photoService;
    }

    @GetMapping("")
    public String mainPage(Model model,
                           Authentication authentication,
                           @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("videos", videoService.getFiveRandomVideos());
        model.addAttribute("photos", photoService.getFiveRandomPhotos());
        model.addAttribute("user", authentication == null ? null : userService.findUserByAuthentication(authentication));
        return "general/main";
    }

    @GetMapping("/videos")
    public String mainVideosPage(Model model,
                                 Authentication authentication,
                                 String search,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        typeAndSortService.getVideosPage(search, sort, model, null);
        return "general/videos";
    }

    @GetMapping("/photos")
    public String mainPhotosPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        typeAndSortService.getPhotosPage(sort, model, null);
        return "general/photos";
    }

    @GetMapping("/users")
    public String mainUsersPage(Model model,
                                Authentication authentication,
                                String search,
                                @RequestParam(value = "sort", defaultValue = "pop") String sort){
        if (search != null)
            model.addAttribute("users", userService.findBySearch(search.toLowerCase()));
        else model.addAttribute("users", userService.findAll());
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        return "general/allUsers";
    }

    @GetMapping("/subscriptions")
    public String mainSubscriptionsPage(Model model,
                                        Authentication authentication,
                                        String search){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        model.addAttribute("authUser", userService.findUserByAuthentication(authentication));
        if (search != null){
            model.addAttribute("followUsers", userService.findBySearchUserAuthFromSubscriptions(userService.findUserByAuthentication(authentication), search.toLowerCase()));
            model.addAttribute("videos", videoService.getAllVideosForLastWeekFromAllFollowUsersByFollowUserSearch(userService.findUserByAuthentication(authentication), search.toLowerCase()));
        }
        else {
            model.addAttribute("followUsers", userService.findAllUserByUserAuthFromSubscriptions(userService.findUserByAuthentication(authentication)));
            model.addAttribute("videos", videoService.getAllVideosForLastWeekFromAllFollowUsers(userService.findUserByAuthentication(authentication)));
        }
        return "general/subscriptions";
    }
}
