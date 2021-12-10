package ru.osminkin.springvideohosting.controller;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.MessageRepository;
import ru.osminkin.springvideohosting.repository.UserRepository;
import ru.osminkin.springvideohosting.services.MessageService;
import ru.osminkin.springvideohosting.services.UserService;

import javax.swing.text.View;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage(Model model,
                                 Authentication authentication){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        model.addAttribute("messages", messageService.findAllMessages());
//        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
//        model.addAttribute("user", user);
//        model.addAttribute("messages", messageRepository.findLast10());
        return "success";
    }

    @GetMapping("/success/{id}")
    @ResponseBody
    public Iterable<Message> load(@PathVariable("id") Long id){
        return messageRepository.findAllByIdBetweenOrderByIdDesc(id-1L, id);
    }

    @PostMapping("/success")
    public String postSuccessPage(Authentication authentication,
                                  @RequestParam("file") MultipartFile file,
                                  @ModelAttribute("messageForm") Message messageFromForm) throws IOException {
        messageService.setMessageFromForm(userService.findUserByEmail(authentication), file, messageFromForm);
        messageService.save(messageFromForm);
        return "redirect:/auth/success";
    }
}
