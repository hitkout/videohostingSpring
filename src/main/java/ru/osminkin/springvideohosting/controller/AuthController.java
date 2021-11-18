package ru.osminkin.springvideohosting.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage(Model model, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        model.addAttribute("userInfo", user);
        return "success";
    }

    @PostMapping("/success")
    public String postSuccessPage(@RequestParam("file") MultipartFile file,
                                  @ModelAttribute("messageForm") Message messageFromForm,
                                  Model model) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            messageFromForm.setFilename(resultFilename);
        }
        messageRepository.save(messageFromForm);
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "success";
    }
}
