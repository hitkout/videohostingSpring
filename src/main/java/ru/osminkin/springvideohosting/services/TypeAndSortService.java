package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.Objects;

@Service
public class TypeAndSortService {
    private final VideoService videoService;
    private final MessageService messageService;
    private final PhotoService photoService;

    public TypeAndSortService(VideoService videoService, MessageService messageService, PhotoService photoService) {
        this.videoService = videoService;
        this.messageService = messageService;
        this.photoService = photoService;
    }

    public void getPage(String type, String sort, Model model, Long userId){
        switch (type) {
            case "videos":
                if (userId == null){
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("videos", videoService.findAllVideosOrderByDate());
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("videos", videoService.findAllVideosOrderByDateDesc());
                    else model.addAttribute("videos", videoService.findAll());
                }
                else {
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("videos", videoService.findAllUserVideosOrderByDate(userId));
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("videos", videoService.findAllUserVideosOrderByDateDesc(userId));
                    else model.addAttribute("videos", videoService.findAllVideosByUserId(userId));
                }
                break;
            case "records":
                if (userId == null){
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("messages", messageService.findAllMessagesOrderByDate());
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("messages", messageService.findAllMessagesOrderByDateDesc());
                    else model.addAttribute("messages", messageService.findAll());
                }
                else {
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("messages", messageService.findAllUserMessagesOrderByDate(userId));
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("messages", messageService.findAllUserMessagesOrderByDateDesc(userId));
                    else model.addAttribute("messages", messageService.findAllUserMessages(userId));
                }
                break;
            default:
                if (userId == null){
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("photos", photoService.findAllPhotosOrderByDate());
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("photos", photoService.findAllPhotosOrderByDateDesc());
                    else model.addAttribute("photos", photoService.findAll());
                }
                else {
                    if (Objects.equals(sort, "new"))
                        model.addAttribute("photos", photoService.findAllUserPhotosOrderByDate(userId));
                    else if (Objects.equals(sort, "old"))
                        model.addAttribute("photos", photoService.findAllUserPhotosOrderByDateDesc(userId));
                    else model.addAttribute("photos", photoService.findAllPhotosByUserId(userId));
                }
                break;
        }
    }
}
