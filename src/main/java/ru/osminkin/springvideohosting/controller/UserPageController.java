package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.services.*;

import java.io.IOException;

@Controller
public class UserPageController {
    private final UserService userService;
    final TypeAndSortService typeAndSortService;

    public UserPageController(UserService userService, TypeAndSortService typeAndSortService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
    }

    @GetMapping("/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId,
                             @RequestParam(value = "sort", defaultValue = "pop") String sort,
                             Authentication authentication,
                             Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByEmail(authentication), userService.findUserById(userId)));
        typeAndSortService.getVideosPage(sort, model, userId);
        return "channel/channelMain";
    }

    @PostMapping(value = "/channel/{userId}", params = "subscribe")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postSubscribe(@PathVariable("userId") long userId,
                                //@RequestParam("id") long id,
                                Authentication authentication) {
        userService.subscribe(userService.findUserByEmail(authentication), userService.findUserById(userId));
        return "redirect:/channel/{userId}";
    }

    @PostMapping(value = "/channel/{userId}", params = "unsubscribe")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postUnsubscribe(@PathVariable("userId") long userId,
                                //@RequestParam("id") long id,
                                Authentication authentication) {
        userService.unsubscribe(userService.findUserByEmail(authentication), userService.findUserById(userId));
        return "redirect:/channel/{userId}";
    }
}