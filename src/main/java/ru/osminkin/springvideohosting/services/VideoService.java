package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.VideoRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.path.video}")
    private String uploadPathVideos;

    public void saveVideoInDb(long userId, MultipartFile file, Video videoFromForm) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathVideos);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            file.transferTo(new File(uploadPathVideos + "/" + uuidFile));
            videoFromForm.setFilename(uuidFile);
            videoFromForm.setUser(userService.findUserById(userId));
            videoRepository.save(videoFromForm);
        }
    }

    public Iterable<Video> findAllPhotosByUserId(Long id){
        return videoRepository.findVideoByUserId(id);
    }
}
