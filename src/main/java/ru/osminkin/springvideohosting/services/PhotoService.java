package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final UserService userService;
    @Value("${upload.path.image}")
    private String uploadPathImages;

    public PhotoService(PhotoRepository photoRepository, UserService userService) {
        this.photoRepository = photoRepository;
        this.userService = userService;
    }

    public void savePhotoInDb(long userId, MultipartFile file, Photo photoFromForm) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathImages);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPathImages + "/" + resultFilename));
            photoFromForm.setFilename(resultFilename);
            photoFromForm.setUser(userService.findUserById(userId));
            photoFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
            photoRepository.save(photoFromForm);
        }
    }

    public Iterable<Photo> findAllPhotosByUserId(Long id){
        return photoRepository.findPhotosByUserId(id);
    }

    public void deletePhotoById(Long id){
        photoRepository.deleteById(id);
    }

    public Iterable<Photo> findAllPhotosOrderByDate(){
        return photoRepository.findAllPhotosOrderByDate();
    }

    public Iterable<Photo> findAllPhotosOrderByDateDesc(){
        return photoRepository.findAllPhotosOrderByDateDesc();
    }

    public Iterable<Photo> findAllUserPhotosOrderByDate(Long id){
        return photoRepository.findAllUserPhotosOrderByDate(id);
    }

    public Iterable<Photo> findAllUserPhotosOrderByDateDesc(Long id){
        return photoRepository.findAllUserPhotosOrderByDateDesc(id);
    }

    public Iterable<Photo> findAll(){
        return photoRepository.findAll();
    }
}
