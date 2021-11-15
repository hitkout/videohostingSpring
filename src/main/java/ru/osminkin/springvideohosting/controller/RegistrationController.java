package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.osminkin.springvideohosting.model.Role;
import ru.osminkin.springvideohosting.model.Status;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String getRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User user){
        //User userFromDb = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return "redirect:/login";
    }
}
