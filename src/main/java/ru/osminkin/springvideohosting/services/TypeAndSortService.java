package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Objects;

@Service
public class TypeAndSortService {
    private final VideoService videoService;
    private final RecordService recordService;
    private final PhotoService photoService;

    public TypeAndSortService(VideoService videoService, RecordService recordService, PhotoService photoService) {
        this.videoService = videoService;
        this.recordService = recordService;
        this.photoService = photoService;
    }

    public void getVideosPage(String sort, Model model, Long userId){
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
    }

    public void getPhotosPage(String sort, Model model, Long userId){
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
    }

    public void getRecordsPage(String sort, Model model, Long userId){
        if (userId == null){
            if (Objects.equals(sort, "new"))
                model.addAttribute("records", recordService.findAllRecordsOrderByDate());
            else if (Objects.equals(sort, "old"))
                model.addAttribute("records", recordService.findAllRecordsOrderByDateDesc());
            else model.addAttribute("records", recordService.findAll());
        }
        else {
            if (Objects.equals(sort, "new"))
                model.addAttribute("records", recordService.findAllUserRecordsOrderByDate(userId));
            else if (Objects.equals(sort, "old"))
                model.addAttribute("records", recordService.findAllUserRecordsOrderByDateDesc(userId));
            else model.addAttribute("records", recordService.findAllUserRecords(userId));
        }
    }
}
