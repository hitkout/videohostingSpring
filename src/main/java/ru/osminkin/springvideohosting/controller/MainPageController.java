package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.services.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

@Controller
public class MainPageController {
    private final UserService userService;
    private final TypeAndSortService typeAndSortService;
    private final VideoService videoService;
    private final RecordService recordService;
    private final PhotoService photoService;

    public MainPageController(UserService userService, TypeAndSortService typeAndSortService, VideoService videoService, RecordService recordService, PhotoService photoService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
        this.videoService = videoService;
        this.recordService = recordService;
        this.photoService = photoService;
    }

    @GetMapping("")
    public String mainPage(Model model,
                           Authentication authentication,
                           @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        model.addAttribute("videos", videoService.getFiveRandomVideos());
        model.addAttribute("photos", photoService.getFiveRandomPhotos());
        model.addAttribute("user", authentication == null ? null : userService.findUserByEmail(authentication));
        model.addAttribute("records", recordService.getFiveRandomRecords());
        return "general/main";
    }

    @GetMapping("/videos")
    public String mainVideosPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        typeAndSortService.getVideosPage(sort, model, null);
        return "general/videos";
    }

    @GetMapping("/photos")
    public String mainPhotosPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        typeAndSortService.getPhotosPage(sort, model, null);
        return "general/photos";
    }

    @GetMapping("/records")
    public String mainRecordsPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        typeAndSortService.getRecordsPage(sort, model, null);
        return "general/records";
    }

    @GetMapping("/users")
    public String mainUsersPage(Model model,
                                Authentication authentication,
                                String search,
                                @RequestParam(value = "sort", defaultValue = "pop") String sort){
        if (search != null)
            model.addAttribute("users", userService.findBySearch(search.toLowerCase()));
        else model.addAttribute("users", userService.findAll());
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        return "general/allUsers";
    }

    @GetMapping("/subscriptions")
    public String mainSubscriptionsPage(Model model,
                                        Authentication authentication){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        model.addAttribute("authUser", userService.findUserByEmail(authentication));
        model.addAttribute("followUsers", userService.findAllUserByUserAuthFromSubscriptions(userService.findUserByEmail(authentication)));
        model.addAttribute("videos", videoService.getAllVideosForLastWeekFromAllFollowUsers(userService.findUserByEmail(authentication), Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime()))));
        return "general/subscriptions";
    }
}
