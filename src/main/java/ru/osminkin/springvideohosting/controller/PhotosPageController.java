package ru.osminkin.springvideohosting.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.services.PhotoService;
import ru.osminkin.springvideohosting.services.TypeAndSortService;
import ru.osminkin.springvideohosting.services.UserService;

import java.io.IOException;

@Controller
public class PhotosPageController {
    private final TypeAndSortService typeAndSortService;
    private final UserService userService;
    private final PhotoService photoService;

    public PhotosPageController(TypeAndSortService typeAndSortService, UserService userService, PhotoService photoService) {
        this.typeAndSortService = typeAndSortService;
        this.userService = userService;
        this.photoService = photoService;
    }

    @GetMapping("/channel/{userId}/photos")
    public String getChannelPhoto(@PathVariable("userId") long userId,
                                  @RequestParam(value = "sort", defaultValue = "pop") String sort,
                                  Authentication authentication,
                                  Model model){
        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("authUser", authentication == null ? null : userService.findUserByAuthentication(authentication));
        model.addAttribute("follow", authentication == null ? null : userService.isSubscribe(userService.findUserByAuthentication(authentication), userService.findUserById(userId)));
        model.addAttribute("subscribers", userService.getSubscribersCount(userService.findUserById(userId)));
        typeAndSortService.getPhotosPage(sort, model, userId);
        //model.addAttribute("photos", photoRepository.findLast10());
        //model.addAttribute("lastElement", photoRepository.getLastId());
        return "channel/channelPhotos";
    }

//    @GetMapping("/auth/success/channel/{userId}/photos/{idPhoto}")
//    @ResponseBody
//    public Iterable<Photo> loadPhoto(@PathVariable("userId") Long userId,
//                                     @PathVariable("idPhoto") Long idPhoto){
//        while (photoRepository.findById(idPhoto).isEmpty())
//            idPhoto--;
//        return photoRepository.findAllPhotosByIdBetweenOrderByIdDesc(idPhoto-2L, idPhoto);
//    }

    @PostMapping(value = "/channel/{userId}/photos", params = "savePhoto")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String postChannelPhoto(@PathVariable("userId") long userId,
                                   @RequestParam("file") MultipartFile file,
                                   @ModelAttribute("photoFromForm") Photo photoFromForm) throws IOException {
        photoService.savePhotoInDb(userId, file, photoFromForm);
        return "redirect:/channel/{userId}/photos";
    }

    @PostMapping(value = "/channel/{userId}/photos", params = "deletePhoto")
    @PreAuthorize("@authenticatedUserService.hasId(#userId)")
    public String deletePhoto(@PathVariable("userId") long userId,
                              @RequestParam("photoId") long photoId){
        photoService.deletePhotoById(photoId);
        return "redirect:/channel/{userId}/photos";
    }
}
