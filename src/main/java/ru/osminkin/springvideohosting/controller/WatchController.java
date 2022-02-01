package ru.osminkin.springvideohosting.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.services.*;

@Controller
public class WatchController {
    private final VideoService videoService;
    private final UserService userService;
    private final RecordService recordService;
    private final LikeAndDislikeVideoForUserService likeAndDislikeVideoForUserService;
    private final NumberWatchVideoService numberWatchVideoService;

    public WatchController(VideoService videoService, UserService userService, RecordService recordService, LikeAndDislikeVideoForUserService likeAndDislikeVideoForUserService, NumberWatchVideoService numberWatchVideoService) {
        this.videoService = videoService;
        this.userService = userService;
        this.recordService = recordService;
        this.likeAndDislikeVideoForUserService = likeAndDislikeVideoForUserService;
        this.numberWatchVideoService = numberWatchVideoService;
    }

    @GetMapping("/watch/{videoId}")
    public String watchPage(Model model,
                            Authentication authentication,
                            @PathVariable("videoId") Long videoId){
        numberWatchVideoService.watch(videoId);
        model.addAttribute("user", videoService.findVideoById(videoId).getUser());
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByAuthentication(authentication), userService.findUserById(videoService.findVideoById(videoId).getUser().getId())));
        model.addAttribute("video", videoService.findVideoById(videoId));
        model.addAttribute("videos", videoService.findRandomVideosWithoutSelectedVideo(videoId));
        model.addAttribute("comments", recordService.findAllCommentsByVideoId(videoId));
        model.addAttribute("subscribers", userService.getSubscribersCount(videoService.findVideoById(videoId).getUser()));
        model.addAttribute("number", numberWatchVideoService.getNumber(videoId));
        if (authentication != null)
            model.addAttribute("isLikeOrDislike", likeAndDislikeVideoForUserService.isLikeOrDislike(userService.findUserByAuthentication(authentication), videoId));
        return "general/watch";
    }

    @PostMapping(value = "/watch/{videoId}", params = "like")
    public String postLike(Authentication authentication,
                                    @PathVariable("videoId") Long videoId){
        likeAndDislikeVideoForUserService.likeVideo(userService.findUserByAuthentication(authentication), videoId);
        return "redirect:/watch/{videoId}";
    }

    @PostMapping(value = "/watch/{videoId}", params = "dislike")
    public String postDislike(Authentication authentication,
                                    @PathVariable("videoId") Long videoId){
        likeAndDislikeVideoForUserService.dislikeVideo(userService.findUserByAuthentication(authentication), videoId);
        return "redirect:/watch/{videoId}";
    }

    @PostMapping(value = "/watch/{videoId}", params = "delete")
    public String postDelete(@PathVariable("videoId") Long videoId,
                             @RequestParam("id") long id){
        likeAndDislikeVideoForUserService.deleteAllRecordsAboutVideo(videoId);
        videoService.deleteVideoById(id);
        return "redirect:/";
    }

    @PostMapping(value = "/watch/{videoId}", params = "deleteComment")
    public String postDeleteComment(@PathVariable("videoId") Long videoId,
                             @RequestParam("commentId") long commentId){
        recordService.deleteRecordById(commentId);
        return "redirect:/watch/{videoId}";
    }

    @PostMapping(value = "/watch/{videoId}", params = "addComment")
    public String postComment(Authentication authentication,
                              @PathVariable("videoId") Long videoId,
                              @ModelAttribute("commentFromForm") Record record){
        recordService.saveComment(userService.findUserByAuthentication(authentication), videoService.findVideoById(videoId), record);
        return "redirect:/watch/{videoId}";
    }
}
