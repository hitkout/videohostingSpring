package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.UserVotesVideoRepository;
import ru.osminkin.springvideohosting.repository.VideoRepository;

import java.util.Objects;

@Service
public class UserVotesVideoService {
    private final UserVotesVideoRepository userVotesVideoRepository;
    private final VideoRepository videoRepository;

    public UserVotesVideoService(UserVotesVideoRepository userVotesVideoRepository, VideoRepository videoRepository) {
        this.userVotesVideoRepository = userVotesVideoRepository;
        this.videoRepository = videoRepository;
    }

    public String getLikeOrDislike(User authUser, Long videoId){
        return userVotesVideoRepository.getLikeOrDislike(authUser, videoId);
    }

    public void likeVideo(User authUser, Long videoId){
        Long likes = videoRepository.getLikesVideo(videoId);
        Long dislikes = videoRepository.getDislikesVideo(videoId);

        if (authUser != videoRepository.findVideoById(videoId).getUser()){
            if (userVotesVideoRepository.getLikeOrDislike(authUser, videoId) == null){
                userVotesVideoRepository.createRecordAndLike(authUser, videoId);
                videoRepository.likeVideo(videoId, ++likes);
            }
            else if (Objects.equals(userVotesVideoRepository.getLikeOrDislike(authUser, videoId), "dislike")){
                userVotesVideoRepository.likeVideo(authUser, videoId);
                videoRepository.dislikeVideo(videoId, --dislikes);
                videoRepository.likeVideo(videoId, ++likes);
            }
            else if (Objects.equals(userVotesVideoRepository.getLikeOrDislike(authUser, videoId), "like")){
                userVotesVideoRepository.deleteRecord(authUser, videoId);
                videoRepository.likeVideo(videoId, --likes);
            }
        }
    }

    public void dislikeVideo(User authUser, Long videoId){
        Long likes = videoRepository.getLikesVideo(videoId);
        Long dislikes = videoRepository.getDislikesVideo(videoId);

        if (authUser != videoRepository.findVideoById(videoId).getUser()) {
            if (userVotesVideoRepository.getLikeOrDislike(authUser, videoId) == null) {
                userVotesVideoRepository.createRecordAndDislike(authUser, videoId);
                videoRepository.dislikeVideo(videoId, ++dislikes);
            } else if (Objects.equals(userVotesVideoRepository.getLikeOrDislike(authUser, videoId), "like")) {
                userVotesVideoRepository.dislikeVideo(authUser, videoId);
                videoRepository.dislikeVideo(videoId, ++dislikes);
                videoRepository.likeVideo(videoId, --likes);
            } else if (Objects.equals(userVotesVideoRepository.getLikeOrDislike(authUser, videoId), "dislike")) {
                userVotesVideoRepository.deleteRecord(authUser, videoId);
                videoRepository.dislikeVideo(videoId, --dislikes);
            }
        }
    }

    public void deleteAllRecordsAboutVideo(Long videoId){
        userVotesVideoRepository.deleteAllRecordsAboutVideo(videoId);
    }
}
