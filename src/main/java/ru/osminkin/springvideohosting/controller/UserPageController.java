package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.services.*;

@Controller
public class UserPageController {
    private final UserService userService;
    final TypeAndSortService typeAndSortService;

    public UserPageController(UserService userService, TypeAndSortService typeAndSortService) {
        this.userService = userService;
        this.typeAndSortService = typeAndSortService;
    }

    @GetMapping("/auth/success/channel/{userId}")
    //@PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String getChannel(@PathVariable("userId") long userId,
                             @RequestParam(value = "sort", defaultValue = "pop") String sort,
                             Authentication authentication,
                             Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", userService.findUserByEmail(authentication));
        typeAndSortService.getVideosPage(sort, model, userId);
        return "channel/channelMain";
    }
}