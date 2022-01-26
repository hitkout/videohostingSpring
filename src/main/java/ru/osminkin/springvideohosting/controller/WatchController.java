package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.osminkin.springvideohosting.services.UserService;
import ru.osminkin.springvideohosting.services.VideoService;

@Controller
public class WatchController {
    private final VideoService videoService;
    private final UserService userService;

    public WatchController(VideoService videoService, UserService userService) {
        this.videoService = videoService;
        this.userService = userService;
    }

    @GetMapping("/watch/{videoId}")
    public String watchPage(Model model,
                            Authentication authentication,
                            @PathVariable("videoId") Long videoId){
        model.addAttribute("user", videoService.findVideoById(videoId).getUser());
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByEmail(authentication), userService.findUserById(videoService.findVideoById(videoId).getUser().getId())));
        model.addAttribute("video", videoService.findVideoById(videoId));
        return "general/watch";
    }
}
