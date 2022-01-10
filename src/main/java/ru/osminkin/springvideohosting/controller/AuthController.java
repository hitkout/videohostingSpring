package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.repository.MessageRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import ru.osminkin.springvideohosting.services.MessageService;
import ru.osminkin.springvideohosting.services.StreamingService;
import ru.osminkin.springvideohosting.services.UserService;
import ru.osminkin.springvideohosting.services.VideoService;

import java.util.Objects;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage(Model model,
                                 Authentication authentication,
                                 @RequestParam(value = "type", defaultValue = "videos") String type,
                                 @RequestParam(value = "sort", defaultValue = "pop") String sort){
        model.addAttribute("user", userService.findUserByEmail(authentication));
        switch (type) {
            case "videos":
                if (Objects.equals(sort, "new")) model.addAttribute("videos", videoRepository.findAll());
                else if (Objects.equals(sort, "old")) model.addAttribute("videos", videoRepository.findAll());
                else model.addAttribute("videos", videoRepository.findAll());
                break;
            case "records":
                if (Objects.equals(sort, "new")) model.addAttribute("messages", messageRepository.findAllOldMessages());
                else if (Objects.equals(sort, "old")) model.addAttribute("messages", messageRepository.findAll());
                else model.addAttribute("messages", messageRepository.findLast10());
                break;
            default:
                if (Objects.equals(sort, "new")) model.addAttribute("messages", messageRepository.findAllOldMessages());
                else if (Objects.equals(sort, "old")) model.addAttribute("messages", messageRepository.findAll());
                else model.addAttribute("messages", messageRepository.findLast10());
                break;
        }

        return "success";
    }

    @GetMapping("/success/{id}")
    @ResponseBody
    public Iterable<Message> load(@PathVariable("id") Long id){
        while (messageRepository.findById(id).isEmpty())
            id--;
        return messageRepository.findAllByIdBetweenOrderByIdDesc(id-2L, id);
    }

    @PostMapping("/success")
    public String postSuccessPage(@ModelAttribute("messageForm") Message messageFromForm) {
        messageService.save(messageFromForm);
        return "redirect:/auth/success";
    }
}
