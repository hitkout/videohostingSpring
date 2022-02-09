package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.UserVotesVideo;
import ru.osminkin.springvideohosting.model.User;

import javax.transaction.Transactional;

public interface UserVotesVideoRepository extends JpaRepository<UserVotesVideo, Long> {
    @Transactional
    @Modifying
    @Query(value = "update user_votes_video set like_or_dislike = 'like' where user_id = :authUser and video_id = :videoId", nativeQuery = true)
    void likeVideo(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "insert into user_votes_video(like_or_dislike, user_id, video_id) values ('like', :authUser, :videoId)", nativeQuery = true)
    void createRecordAndLike(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Query(value = "select NULLIF(TRIM(like_or_dislike),'') FROM user_votes_video where user_id = :authUser and video_id = :videoId", nativeQuery = true)
    String getLikeOrDislike(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "update user_votes_video set like_or_dislike = 'dislike' where user_id = :authUser and video_id = :videoId", nativeQuery = true)
    void dislikeVideo(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "insert into user_votes_video(like_or_dislike, user_id, video_id) values ('dislike', :authUser, :videoId)", nativeQuery = true)
    void createRecordAndDislike(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "delete from user_votes_video where user_id = :authUser and video_id = :videoId",nativeQuery = true)
    void deleteRecord(@Param("authUser") User authUser, @Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "delete from user_votes_video where video_id = :videoId", nativeQuery = true)
    void deleteAllRecordsAboutVideo(@Param("videoId") Long videoId);
}
