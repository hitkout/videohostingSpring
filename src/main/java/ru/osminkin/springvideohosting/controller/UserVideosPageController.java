package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.services.TypeAndSortService;
import ru.osminkin.springvideohosting.services.UserService;
import ru.osminkin.springvideohosting.services.VideoService;

import java.io.IOException;

@Controller
public class UserVideosPageController {
    private final TypeAndSortService typeAndSortService;
    private final UserService userService;
    private final VideoService videoService;

    public UserVideosPageController(TypeAndSortService typeAndSortService, UserService userService, VideoService videoService) {
        this.typeAndSortService = typeAndSortService;
        this.userService = userService;
        this.videoService = videoService;
    }

    @PostMapping(value = "/channel/{userId}/videos", params = {"videoTitle", "videoDescription"})
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelVideo(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("videoTitle") String videoTitle,
                                   @RequestParam("videoDescription") String videoDescription) throws IOException {
        videoService.saveVideoInDb(userId, file, videoTitle, videoDescription);
        return "redirect:/channel/{userId}?type=videos";
    }

    @PostMapping(value = "/channel/{userId}/videos", params = "deleteVideo")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String deleteVideo(@PathVariable("userId") long userId,
                              @RequestParam("id") long id){
        videoService.deleteVideoById(id);
        return "redirect:/channel/{userId}?type=videos";
    }

    @GetMapping("/channel/{userId}/videos")
    public String getChannelVideos(@PathVariable("userId") long userId,
                                   Authentication authentication,
                                   @RequestParam(value = "search", defaultValue = "") String search,
                                   @RequestParam(value = "sort", defaultValue = "pop") String sort,
                                   Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByAuthentication(authentication), userService.findUserById(userId)));
        model.addAttribute("subscribers", userService.getSubscribersCount(userService.findUserById(userId)));
        typeAndSortService.getVideosPage(search, sort, model, userId);
        return "channel/channelVideos";
    }
}
