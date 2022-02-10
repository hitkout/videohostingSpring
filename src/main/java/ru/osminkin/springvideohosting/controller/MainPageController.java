package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.services.*;

@Controller
public class MainPageController {
    private final UserService userService;
    private final TypeAndSortService typeAndSortService;
    private final VideoService videoService;
    private final PhotoService photoService;

    public MainPageController(UserService userService,
                              TypeAndSortService typeAndSortService,
                              VideoService videoService,
                              PhotoService photoService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
        this.videoService = videoService;
        this.photoService = photoService;
    }

    @GetMapping("")
    public String mainPage(Model model){
        model.addAttribute("authUser", userService.getCurrentUser());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("videos", videoService.getFiveRandomVideos());
        model.addAttribute("photos", photoService.getFiveRandomPhotos());
        return "general/main";
    }

    @GetMapping("/videos")
    public String mainVideosPage(Model model,
                                 String search,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", userService.getCurrentUser());
        typeAndSortService.getVideosPage(search, sort, model, null);
        return "general/videos";
    }

    @GetMapping("/photos")
    public String mainPhotosPage(Model model,
                                 String search,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", userService.getCurrentUser());
        typeAndSortService.getPhotosPage(search, sort, model, null);
        return "general/photos";
    }

    @GetMapping("/users")
    public String mainUsersPage(Model model,
                                String search){
        if (search != null)
            model.addAttribute("users", userService.findBySearch(search.toLowerCase()));
        else model.addAttribute("users", userService.findAll());
        model.addAttribute("authUser", userService.getCurrentUser());
        return "general/allUsers";
    }

    @GetMapping("/subscriptions")
    public String mainSubscriptionsPage(Model model,
                                        String search){
        model.addAttribute("authUser", userService.getCurrentUser());
        if (search != null){
            model.addAttribute("followUsers",
                    userService.findBySearchUserAuthFromSubscriptions(
                            userService.getCurrentUser(), search.toLowerCase()
                    )
            );
            model.addAttribute("videos",
                    videoService.getAllVideosForLastWeekFromAllFollowUsersByFollowUserSearch(
                            userService.getCurrentUser(), search.toLowerCase()
                    )
            );
            model.addAttribute("photos",
                    photoService.getAllPhotosForLastWeekFromAllFollowUsersByFollowUserSearch(
                            userService.getCurrentUser(), search.toLowerCase()
                    )
            );
        }
        else {
            model.addAttribute("followUsers",
                    userService.findAllUserByUserAuthFromSubscriptions(
                            userService.getCurrentUser()
                    )
            );
            model.addAttribute("videos",
                    videoService.getAllVideosForLastWeekFromAllFollowUsers(
                            userService.getCurrentUser()
                    )
            );
            model.addAttribute("photos",
                    photoService.getAllPhotosForLastWeekFromAllFollowUsers(
                            userService.getCurrentUser()
                    )
            );
        }
        return "general/subscriptions";
    }
}
