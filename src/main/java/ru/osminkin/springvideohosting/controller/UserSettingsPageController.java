package ru.osminkin.springvideohosting.controller;

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
    public String getSettingsPage(Model model){
        model.addAttribute("authUser", userService.getCurrentUser());
        model.addAttribute("changePassword", null);
        return "channel/channelSettings";
    }

    @PostMapping(value = "/settings", params = "name")
    public String postChangeName(@RequestParam("name") String name) {
        userService.changeName(userService.getCurrentUser().getId(), name);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "surname")
    public String postChangeSurname(@RequestParam("surname") String surname) {
        userService.changeSurname(userService.getCurrentUser().getId(), surname);
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = {"oldPassword", "newPassword", "newPasswordConfirm"})
    public String postChangeUserPassword(@RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         Model model) {
        model.addAttribute("authUser", userService.getCurrentUser());
        model.addAttribute("changePassword", userService.changeUserPassword(
                userService.getCurrentUser().getId(),
                oldPassword,
                newPassword));

        return "channel/channelSettings";
    }

    @PostMapping(value = "/settings")
    public String postChangeUserPhoto(@RequestParam("file") MultipartFile file,
                                      @ModelAttribute("userPhotoFromForm") Photo userPhotoFromForm) throws IOException {
        if (!Objects.equals(file.getOriginalFilename(), "")){
            if (!userService.findUserById(
                    userService.getCurrentUser().getId()).getPhoto().equals("default.png"))
                userService.deleteUserPhoto(userService.findUserById(userService.getCurrentUser().getId()).getPhoto());
            userService.saveUserPhoto(userService.getCurrentUser().getId(), file);
        }
        return "redirect:/settings";
    }

    @PostMapping(value = "/settings", params = "deleteUserPhoto")
    public String postDeleteUserPhoto() {
        if (!userService.findUserById(
                userService.getCurrentUser().getId()).getPhoto().equals("default.png"))
            userService.deleteUserPhoto(userService.findUserById(userService.getCurrentUser().getId()).getPhoto());
        userService.saveUserPhoto(userService.getCurrentUser().getId(), "default.png");
        return "redirect:/settings";
    }
}
