package ru.osminkin.springvideohosting.controller;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.services.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
public class WatchController {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E',' MMM dd',' yyyy K:mm:ss a");
    private static final Logger LOGGER = Logger.getLogger(WatchController.class);
    private final VideoService videoService;
    private final UserService userService;
    private final RecordService recordService;
    private final UserVotesVideoService userVotesVideoService;
    private final NumberWatchVideoService numberWatchVideoService;

    public WatchController(VideoService videoService,
                           UserService userService,
                           RecordService recordService,
                           UserVotesVideoService userVotesVideoService,
                           NumberWatchVideoService numberWatchVideoService) {
        this.videoService = videoService;
        this.userService = userService;
        this.recordService = recordService;
        this.userVotesVideoService = userVotesVideoService;
        this.numberWatchVideoService = numberWatchVideoService;
    }

    @GetMapping("/watch/{videoId}")
    public String watchPage(Model model,
                            Authentication authentication,
                            @RequestParam(value = "sort", defaultValue = "new") String sort,
                            @PathVariable("videoId") Long videoId){
        if (videoService.findVideoById(videoId) == null){
            throw new ResourceNotFoundException();
        }
        numberWatchVideoService.watch(videoId);
        LOGGER.debug(DATE_FORMAT.format(new Date()) + " - " + videoId);
        model.addAttribute("user", videoService.findVideoById(videoId).getUser());
        model.addAttribute("authUser", userService.getCurrentUser());
        model.addAttribute("follow", authentication == null
                ? null
                : userService.isSubscribe(userService.getCurrentUser(),
                userService.findUserById(videoService.findVideoById(videoId).getUser().getId())));
        model.addAttribute("video", videoService.findVideoById(videoId));
        model.addAttribute("videos", videoService.findRandomVideosWithoutSelectedVideo(videoId));
        if (Objects.equals(sort, "new"))
            model.addAttribute("comments", recordService.findAllCommentsByNewVideoId(videoId));
        else model.addAttribute("comments", recordService.findAllCommentsByOldVideoId(videoId));
        model.addAttribute("subscribers",
                userService.getSubscribersCount(videoService.findVideoById(videoId).getUser()));
        model.addAttribute("number", numberWatchVideoService.getNumber(videoId));
        if (authentication != null) {
            model.addAttribute("isLikeOrDislike",
                    userVotesVideoService.getLikeOrDislike(userService.getCurrentUser(), videoId));
        }
        return "general/watch";
    }

    @PostMapping(value = "/watch/{videoId}", params = "like")
    public String postLike(@PathVariable("videoId") Long videoId){
        userVotesVideoService.likeVideo(userService.getCurrentUser(), videoId);
        return "redirect:/watch/{videoId}";
    }

    @PostMapping(value = "/watch/{videoId}", params = "dislike")
    public String postDislike(@PathVariable("videoId") Long videoId){
        userVotesVideoService.dislikeVideo(userService.getCurrentUser(), videoId);
        return "redirect:/watch/{videoId}";
    }

    @PostMapping(value = "/watch/{videoId}", params = "delete")
    public String postDelete(@PathVariable("videoId") Long videoId,
                             @RequestParam("id") long id){
        userVotesVideoService.deleteAllRecordsAboutVideo(videoId);
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
    public String postComment(@PathVariable("videoId") Long videoId,
                              @ModelAttribute("commentFromForm") Record record){
        recordService.saveComment(userService.getCurrentUser(), videoService.findVideoById(videoId), record);
        return "redirect:/watch/{videoId}";
    }
}
