package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.repository.RecordRepository;
import ru.osminkin.springvideohosting.services.RecordService;
import ru.osminkin.springvideohosting.services.TypeAndSortService;
import ru.osminkin.springvideohosting.services.UserService;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Controller
public class RecordsPageController {
    private final TypeAndSortService typeAndSortService;
    private final UserService userService;
    private final RecordService recordService;
    private final RecordRepository recordRepository;

    public RecordsPageController(TypeAndSortService typeAndSortService, UserService userService, RecordService recordService, RecordRepository recordRepository) {
        this.typeAndSortService = typeAndSortService;
        this.userService = userService;
        this.recordService = recordService;
        this.recordRepository = recordRepository;
    }

    @PostMapping(value = "/auth/success/channel/{userId}/records", params = "saveMessage")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelMessage(@PathVariable("userId") long userId,
                                     @ModelAttribute("messageFromForm") Record messageFromForm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        messageFromForm.setUser(userService.findUserById(userId));
        messageFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
        recordService.save(messageFromForm);
        return "redirect:/auth/success/channel/{userId}/records";
    }

    @PostMapping(value = "/auth/success/channel/{userId}/records", params = "deleteMessage")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannel(@PathVariable("userId") long userId,
                              @RequestParam("id") long id){
        recordService.deleteRecordById(id);
        return "redirect:/auth/success/channel/{userId}/records";
    }

//    @GetMapping("/success/{id}")
//    @ResponseBody
//    public Iterable<Message> load(@PathVariable("id") Long id){
//        while (messageRepository.findById(id).isEmpty())
//            id--;
//        return messageRepository.findAllByIdBetweenOrderByIdDesc(id-2L, id);
//    }

    @GetMapping("/auth/success/channel/{userId}/records")
    public String getChannelRecords(@PathVariable("userId") Long userId,
                                    @RequestParam(value = "sort", defaultValue = "pop") String sort,
                                    Authentication authentication,
                                    Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", userService.findUserByEmail(authentication));
        typeAndSortService.getRecordsPage(sort, model, userId);
        return "channel/channelRecords";
    }

    @PostMapping("/success")
    public String postSuccessPage(@ModelAttribute("messageForm") Record messageFromForm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        messageFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
        recordService.save(messageFromForm);
        return "redirect:/auth/success/main";
    }
}
