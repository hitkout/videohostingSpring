package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.services.UserService;

import java.io.IOException;

@Controller
public class SettingsPageController {
    private final UserService userService;

    public SettingsPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model,
                                  Authentication authentication){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByEmail(authentication));
        return "channel/channelSettings";
    }

    @PostMapping(value = "/settings", params = "changeName")
    public String postChangeName(Authentication authentication,
                                   @RequestParam("name") String name) {
        userService.changeName(userService.findUserByEmail(authentication).getId(), name);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "changeSurname")
    public String postChangeSurname(Authentication authentication,
                                   @RequestParam("surname") String surname) {
        userService.changeSurname(userService.findUserByEmail(authentication).getId(), surname);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "changeUserPassword")
    public String postChangeUserPassword(Authentication authentication,
                                         @RequestParam("password") String password) {
        userService.changeUserPassword(userService.findUserByEmail(authentication).getId(), password);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "changeUserPhoto")
    public String postChangeUserPhoto(Authentication authentication,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("userPhotoFromForm") Photo userPhotoFromForm) throws IOException {
        if (!userService.findUserById(userService.findUserByEmail(authentication).getId()).getPhoto().equals("default.png"))
            userService.deleteUserPhoto(userService.findUserById(userService.findUserByEmail(authentication).getId()).getPhoto());
        userService.saveUserPhoto(userService.findUserByEmail(authentication).getId(), file);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "deleteUserPhoto")
    public String postDeleteUserPhoto(Authentication authentication) {
        if (!userService.findUserById(userService.findUserByEmail(authentication).getId()).getPhoto().equals("default.png"))
            userService.deleteUserPhoto(userService.findUserById(userService.findUserByEmail(authentication).getId()).getPhoto());
        userService.saveUserPhoto(userService.findUserByEmail(authentication).getId(), "default.png");
        return "redirect:/settings";
    }
}
