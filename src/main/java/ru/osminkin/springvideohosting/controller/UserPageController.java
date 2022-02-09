package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.services.*;

@Controller
public class UserPageController {
    private final UserService userService;
    private final VideoService videoService;
    private final PhotoService photoService;

    public UserPageController(UserService userService, VideoService videoService, PhotoService photoService) {
        this.userService = userService;
        this.videoService = videoService;
        this.photoService = photoService;
    }

    @GetMapping("/channel/{userId}")
    public String getChannel(@PathVariable("userId") long userId,
                             Authentication authentication,
                             Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByAuthentication(authentication), userService.findUserById(userId)));
        model.addAttribute("videos", videoService.findAllVideosByUserId(userId));
        model.addAttribute("photos", photoService.findAllPhotosByUserId(userId));
        model.addAttribute("subscribers", userService.getSubscribersCount(userService.findUserById(userId)));
        model.addAttribute("userStatus", userService.getUserStatus(userId));
        return "channel/channelMain";
    }

    @PostMapping(value = "/channel/{userId}", params = "subscribe")
    public String postSubscribe(@PathVariable("userId") long userId,
                                Authentication authentication) {
        userService.subscribe(userService.findUserByAuthentication(authentication), userService.findUserById(userId));
        return "redirect:/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "banUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String postBanned(@PathVariable("userId") long userId) {
        userService.banUserById(userId);
        return "redirect:/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "unbanUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String postUnbanned(@PathVariable("userId") long userId) {
        userService.unbanUserById(userId);
        return "redirect:/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "unsubscribe")
    public String postUnsubscribe(@PathVariable("userId") long userId,
                                Authentication authentication) {
        userService.unsubscribe(userService.findUserByAuthentication(authentication), userService.findUserById(userId));
        return "redirect:/channel/{userId}";
    }
}