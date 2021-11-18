package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserRepository;

@Controller
@RequestMapping("/auth/success")
public class MyPageController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/userInfo")
    public String myPage(Model model, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        model.addAttribute("userInfo", user);
        return "userPage";
    }
}
