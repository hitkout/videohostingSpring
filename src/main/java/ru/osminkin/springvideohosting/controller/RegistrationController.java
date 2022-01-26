package ru.osminkin.springvideohosting.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.osminkin.springvideohosting.model.Role;
import ru.osminkin.springvideohosting.model.Status;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

@Controller
@RequestMapping("/auth")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setPhoto("default.png");
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return "redirect:/login";
    }
}