package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    @Value("${upload.path.video}")
    private String uploadPathVideos;

    public VideoService(VideoRepository videoRepository, UserService userService) {
        this.videoRepository = videoRepository;
        this.userService = userService;
    }

    public Video findVideoById(Long videoId){
        return videoRepository.findVideoById(videoId);
    }

    public void saveVideoInDb(long userId, MultipartFile file, Video videoFromForm) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathVideos);
            if (!upload.exists()){
                if (upload.mkdir()){
                    System.out.println("Created directory");
                }
            }
            String uuidFile = UUID.randomUUID().toString();
            file.transferTo(new File(uploadPathVideos + "/" + uuidFile + ".mp4"));
            videoFromForm.setFilename(uuidFile);
            videoFromForm.setUser(userService.findUserById(userId));
            videoFromForm.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
            videoRepository.save(videoFromForm);
        }
    }

    public Iterable<Video> findAllVideosByUserId(Long id){
        return videoRepository.findVideoByUserId(id);
    }

    public void deleteVideoById(Long id){
        File file = new File(uploadPathVideos + "/" + videoRepository.findVideoById(id).getFilename() + ".mp4");
        System.out.println(uploadPathVideos + "/" + videoRepository.findVideoById(id).getFilename() + ".mp4");
        if (file.delete()){
            System.out.println("Video deleted");
        }
        videoRepository.deleteById(id);
    }

    public Iterable<Video> findAll(){
        return videoRepository.findAll();
    }

    public Iterable<Video> findAllVideosOrderByDateDesc(){
        return videoRepository.findAllVideosOrderByDateDesc();
    }

    public Iterable<Video> findAllVideosOrderByDate(){
        return videoRepository.findAllVideosOrderByDate();
    }

    public Iterable<Video> findAllUserVideosOrderByDateDesc(Long id){
        return videoRepository.findAllUserVideosOrderByDateDesc(id);
    }

    public Iterable<Video> findAllUserVideosOrderByDate(Long id){
        return videoRepository.findAllUserVideosOrderByDate(id);
    }

    public List<Video> getFiveRandomVideos(){
        return videoRepository.getFiveRandomVideos();
    }

    public List<Video> getAllVideosFromAllFollowUsers(User follower){
        return videoRepository.getAllVideosFromAllFollowUsers(follower);
    }

    public List<Video> getAllVideosForLastWeekFromAllFollowUsers(User follower, Timestamp dateNow){
        return videoRepository.getAllVideosForLastWeekFromAllFollowUsers(follower);
    }

    public List<Video> findRandomVideosWithoutSelectedVideo(Long videoId){
        return videoRepository.findRandomVideosWithoutSelectedVideo(videoId);
    }
}
