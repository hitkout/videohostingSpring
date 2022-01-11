package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.repository.MessageRepository;
import ru.osminkin.springvideohosting.services.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final TypeAndSortService typeAndSortService;

    public AuthController(UserService userService, MessageService messageService, MessageRepository messageRepository, TypeAndSortService typeAndSortService) {
        this.userService = userService;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.typeAndSortService = typeAndSortService;
    }

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
        typeAndSortService.getPage(type, sort, model, null);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        messageFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
        messageService.save(messageFromForm);
        return "redirect:/auth/success";
    }
}
