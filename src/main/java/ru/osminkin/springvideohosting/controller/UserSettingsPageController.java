package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.services.UserService;

import java.io.IOException;
import java.util.Objects;

@Controller
public class UserSettingsPageController {
    private final UserService userService;

    public UserSettingsPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model,
                                  Authentication authentication){
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("changePassword", null);
        return "channel/channelSettings";
    }

    @PostMapping(value = "/settings", params = "name")
    public String postChangeName(Authentication authentication,
                                   @RequestParam("name") String name) {
        userService.changeName(userService.findUserByAuthentication(authentication).getId(), name);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "surname")
    public String postChangeSurname(Authentication authentication,
                                   @RequestParam("surname") String surname) {
        userService.changeSurname(userService.findUserByAuthentication(authentication).getId(), surname);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = {"oldPassword", "newPassword", "newPasswordConfirm"})
    public String postChangeUserPassword(Authentication authentication,
                                         @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         Model model) {
        model.addAttribute("authUser", userService.findUserByAuthentication(authentication));
        model.addAttribute("changePassword", userService.changeUserPassword(
                userService.findUserByAuthentication(authentication).getId(),
                oldPassword,
                newPassword));

        return "channel/channelSettings";
    }

    @PostMapping(value = "/settings")
    public String postChangeUserPhoto(Authentication authentication,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("userPhotoFromForm") Photo userPhotoFromForm) throws IOException {
        if (!Objects.equals(file.getOriginalFilename(), "")){
            if (!userService.findUserById(userService.findUserByAuthentication(authentication).getId()).getPhoto().equals("default.png"))
                userService.deleteUserPhoto(userService.findUserById(userService.findUserByAuthentication(authentication).getId()).getPhoto());
            userService.saveUserPhoto(userService.findUserByAuthentication(authentication).getId(), file);
        }
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "deleteUserPhoto")
    public String postDeleteUserPhoto(Authentication authentication) {
        if (!userService.findUserById(userService.findUserByAuthentication(authentication).getId()).getPhoto().equals("default.png"))
            userService.deleteUserPhoto(userService.findUserById(userService.findUserByAuthentication(authentication).getId()).getPhoto());
        userService.saveUserPhoto(userService.findUserByAuthentication(authentication).getId(), "default.png");
        return "redirect:/settings";
    }
}