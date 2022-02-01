package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.LikeAndDislikeVideoForUserRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;

import java.util.Objects;

@Service
public class LikeAndDislikeVideoForUserService {
    private final LikeAndDislikeVideoForUserRepository likeAndDislikeVideoForUserRepository;
    private final VideoRepository videoRepository;

    public LikeAndDislikeVideoForUserService(LikeAndDislikeVideoForUserRepository likeAndDislikeVideoForUserRepository, VideoRepository videoRepository) {
        this.likeAndDislikeVideoForUserRepository = likeAndDislikeVideoForUserRepository;
        this.videoRepository = videoRepository;
    }

    public String isLikeOrDislike(User authUser, Long videoId){
        return likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId);
    }

    public void likeVideo(User authUser, Long videoId){
        Long likes = videoRepository.getLikesVideo(videoId);
        Long dislikes = videoRepository.getDislikesVideo(videoId);

        if (authUser != videoRepository.findVideoById(videoId).getUser()){
            if (likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId) == null){
                likeAndDislikeVideoForUserRepository.createRecordAndLike(authUser, videoId);
                videoRepository.likeVideo(videoId, ++likes);
            }
            else if (Objects.equals(likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId), "dislike")){
                likeAndDislikeVideoForUserRepository.likeVideo(authUser, videoId);
                videoRepository.dislikeVideo(videoId, --dislikes);
                videoRepository.likeVideo(videoId, ++likes);
            }
            else if (Objects.equals(likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId), "like")){
                likeAndDislikeVideoForUserRepository.deleteRecord(authUser, videoId);
                videoRepository.likeVideo(videoId, --likes);
            }
        }
    }

    public void dislikeVideo(User authUser, Long videoId){
        Long likes = videoRepository.getLikesVideo(videoId);
        Long dislikes = videoRepository.getDislikesVideo(videoId);

        if (authUser != videoRepository.findVideoById(videoId).getUser()) {
            if (likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId) == null) {
                likeAndDislikeVideoForUserRepository.createRecordAndDislike(authUser, videoId);
                videoRepository.dislikeVideo(videoId, ++dislikes);
            } else if (Objects.equals(likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId), "like")) {
                likeAndDislikeVideoForUserRepository.dislikeVideo(authUser, videoId);
                videoRepository.dislikeVideo(videoId, ++dislikes);
                videoRepository.likeVideo(videoId, --likes);
            } else if (Objects.equals(likeAndDislikeVideoForUserRepository.isLikeOrDislike(authUser, videoId), "dislike")) {
                likeAndDislikeVideoForUserRepository.deleteRecord(authUser, videoId);
                videoRepository.dislikeVideo(videoId, --dislikes);
            }
        }
    }

    public void deleteAllRecordsAboutVideo(Long videoId){
        likeAndDislikeVideoForUserRepository.deleteAllRecordsAboutVideo(videoId);
    }
}
