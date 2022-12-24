package ru.osminkin.springvideohosting.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.osminkin.springvideohosting.model.Role;
import ru.osminkin.springvideohosting.model.Status;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

import java.util.Optional;

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
    public String getRegistrationPage(Model model){
        model.addAttribute("userExists", false);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User user,
                               Model model){
        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser.isPresent()){
            model.addAttribute("userExists", true);
            return "registration";
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            user.setPhoto("default.png");
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
            return "redirect:/login";
        }
    }
}