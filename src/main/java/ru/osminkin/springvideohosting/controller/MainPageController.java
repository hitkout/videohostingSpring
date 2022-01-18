package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.services.TypeAndSortService;
import ru.osminkin.springvideohosting.services.UserService;

@Controller
@RequestMapping("/auth/success")
public class MainPageController {
    private final UserService userService;
    private final TypeAndSortService typeAndSortService;

    public MainPageController(UserService userService, TypeAndSortService typeAndSortService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
    }

    @GetMapping("/main")
    public String mainPage(Model model,
                           Authentication authentication,
                           @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        typeAndSortService.getVideosPage(sort, model, null);
        return "general/main";
    }

    @GetMapping("/main/videos")
    public String mainVideosPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        typeAndSortService.getVideosPage(sort, model, null);
        return "general/videos";
    }

    @GetMapping("/main/photos")
    public String mainPhotosPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        typeAndSortService.getPhotosPage(sort, model, null);
        return "general/photos";
    }

    @GetMapping("/main/records")
    public String mainRecordsPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        typeAndSortService.getRecordsPage(sort, model, null);
        return "general/records";
    }
}
