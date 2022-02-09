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

    public void getVideosPage(String search, String sort, Model model, Long userId){
        if (userId == null){
            if (Objects.equals(sort, "new")){
                if (search != null)
                    model.addAttribute("videos", videoService.findAllVideosOrderByDate(search));
                else model.addAttribute("videos", videoService.findAllVideosOrderByDate());
            }
            else if (Objects.equals(sort, "old")){
                if (search != null)
                    model.addAttribute("videos", videoService.findAllVideosOrderByDateDesc(search));
                else model.addAttribute("videos", videoService.findAllVideosOrderByDateDesc());
            }
            else {
                if (search != null)
                    model.addAttribute("videos", videoService.findAllVideos(search));
                else model.addAttribute("videos", videoService.findAllVideos());
            }
        }
        else {
            if (Objects.equals(sort, "new")){
                if (search != null)
                    model.addAttribute("videos", videoService.findAllUserVideosOrderByDate(userId, search));
                else model.addAttribute("videos", videoService.findAllUserVideosOrderByDate(userId));
            }
            else if (Objects.equals(sort, "old")){
                if (search != null)
                    model.addAttribute("videos", videoService.findAllUserVideosOrderByDateDesc(userId, search));
                else model.addAttribute("videos", videoService.findAllUserVideosOrderByDateDesc(userId));
            }
            else {
                if (search != null)
                    model.addAttribute("videos", videoService.findAllVideosByUserId(userId, search));
                else model.addAttribute("videos", videoService.findAllVideosByUserId(userId));
            }
        }
    }

    public void getPhotosPage(String sort, Model model, Long userId){
        if (userId == null){
            if (Objects.equals(sort, "new"))
                model.addAttribute("photos", photoService.findAllPhotosOrderByDate());
            else if (Objects.equals(sort, "old"))
                model.addAttribute("photos", photoService.findAllPhotosOrderByDateDesc());
            else model.addAttribute("photos", photoService.findAllPhotos());
        }
        else {
            if (Objects.equals(sort, "new"))
                model.addAttribute("photos", photoService.findAllUserPhotosOrderByDate(userId));
            else if (Objects.equals(sort, "old"))
                model.addAttribute("photos", photoService.findAllUserPhotosOrderByDateDesc(userId));
            else model.addAttribute("photos", photoService.findAllPhotosByUserId(userId));
        }
    }
}
