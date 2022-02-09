package ru.osminkin.springvideohosting.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.NumberWatchVideoRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final RecordService recordService;
    @Value("${upload.path.video}")
    private String uploadPathVideos;
    private final NumberWatchVideoRepository numberWatchVideoRepository;

    public VideoService(VideoRepository videoRepository, UserService userService, RecordService recordService, NumberWatchVideoRepository numberWatchVideoRepository) {
        this.videoRepository = videoRepository;
        this.userService = userService;
        this.recordService = recordService;
        this.numberWatchVideoRepository = numberWatchVideoRepository;
    }

    public Video findVideoById(Long videoId){
        return videoRepository.findVideoById(videoId);
    }

    public void saveVideoInDb(long userId, MultipartFile file, String videoTitle, String videoDescription) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Video video = new Video();
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathVideos);
            if (!upload.exists()){
                if (upload.mkdir()){
                    log.info("Dir {} created", upload.getName());
                }
            }
            String uuidFile = UUID.randomUUID().toString();
            file.transferTo(new File(uploadPathVideos + "/" + uuidFile + ".mp4"));
            video.setFilename(uuidFile);
            video.setUser(userService.findUserById(userId));
            video.setVideoTitle(videoTitle);
            video.setVideoDescription(videoDescription);
            video.setLikes(0L);
            video.setDislikes(0L);
            video.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
            videoRepository.save(video);
        }
    }

    public Iterable<Video> findAllVideosByUserId(Long id){
        return videoRepository.findVideosByUserId(id);
    }

    public Iterable<Video> findAllVideosByUserId(Long id, String search){
        return videoRepository.findAllVideosByUserId(id, search);
    }

    public Iterable<Video> findAllVideos(String search){
        return videoRepository.findAllVideos(search);
    }

    public Iterable<Video> findAllVideos(){
        return videoRepository.findAllVideos();
    }

    public void deleteVideoById(Long id){
        numberWatchVideoRepository.deleteByVideoId(id);
        recordService.deleteByVideoId(id);
        File file = new File(uploadPathVideos + "/" + videoRepository.findVideoById(id).getFilename() + ".mp4");
        if (file.delete()){
            log.info("Video {} deleted", file.getName());
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

    public Iterable<Video> findAllVideosOrderByDateDesc(String search){
        return videoRepository.findAllVideosOrderByDateDesc(search);
    }

    public Iterable<Video> findAllVideosOrderByDate(String search){
        return videoRepository.findAllVideosOrderByDate(search);
    }

    public Iterable<Video> findAllUserVideosOrderByDateDesc(Long id){
        return videoRepository.findAllUserVideosOrderByDateDesc(id);
    }

    public Iterable<Video> findAllUserVideosOrderByDateDesc(Long id, String search){
        return videoRepository.findAllUserVideosOrderByDateDesc(id, search);
    }

    public Iterable<Video> findAllUserVideosOrderByDate(Long id){
        return videoRepository.findAllUserVideosOrderByDate(id);
    }

    public Iterable<Video> findAllUserVideosOrderByDate(Long id, String search){
        return videoRepository.findAllUserVideosOrderByDate(id, search);
    }

    public List<Video> getFiveRandomVideos(){
        return videoRepository.getFiveRandomVideos();
    }

    public List<Video> getAllVideosFromAllFollowUsers(User follower){
        return videoRepository.getAllVideosFromAllFollowUsers(follower);
    }

    public List<Video> getAllVideosForLastWeekFromAllFollowUsers(User follower){
        return videoRepository.getAllVideosForLastWeekFromAllFollowUsers(follower);
    }

    public List<Video> getAllVideosForLastWeekFromAllFollowUsersByFollowUserSearch(User follower, String search){
        return videoRepository.getAllVideosForLastWeekFromAllFollowUsersByFollowUserSearch(follower, search);
    }

    public List<Video> findRandomVideosWithoutSelectedVideo(Long videoId){
        return videoRepository.findRandomVideosWithoutSelectedVideo(videoId);
    }
}
