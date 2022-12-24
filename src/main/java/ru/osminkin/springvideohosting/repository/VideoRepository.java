package ru.osminkin.springvideohosting.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import javax.transaction.Transactional;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    @NonNull
    Optional<Video> findById(@NonNull Long id);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.user_id = :id", nativeQuery = true)
    Iterable<Video> findVideosByUserId(Long id);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where v.user_id = :id and u.status = 'ACTIVE' and (lower(video_title) like %:search% or lower(video_description) like %:search%)", nativeQuery = true)
    Iterable<Video> findAllVideosByUserId(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDateDesc();

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and (lower(video_title) like %:search% or lower(video_description) like %:search%)", nativeQuery = true)
    Iterable<Video> findAllVideos(@Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE'", nativeQuery = true)
    Iterable<Video> findAllVideos();

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and (lower(video_title) like %:search% or lower(video_description) like %:search%) order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDateDesc(@Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' order by add_date", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDate();

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.user_id = :id order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and (lower(video_title) like %:search% or lower(video_description) like %:search%) order by add_date", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDate(@Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.user_id = :id and (lower(video_title) like %:search% or lower(video_description) like %:search%) order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDateDesc(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.user_id = :id order by add_date", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDate(@Param("id") Long id);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.user_id = :id and (lower(video_title) like %:search% or lower(video_description) like %:search%) order by add_date", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDate(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.id = :id", nativeQuery = true)
    Video findVideoById(@Param("id") Long id);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' order by random() limit 5", nativeQuery = true)
    Iterable<Video> getFiveRandomVideos();

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id inner join subscriptions s on u.id = s.follow_user where u.status = 'ACTIVE' and s.follower = :follower and v.user_id = s.follow_user", nativeQuery = true)
    Iterable<Video> getAllVideosFromAllFollowUsers(@Param("follower") User follower);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id inner join subscriptions s on u.id = s.follow_user where u.status = 'ACTIVE' and s.follower = :follower and v.user_id = s.follow_user and v.add_date >= (now() at time zone 'UTC' - interval '7 day')", nativeQuery = true)
    Iterable<Video> getAllVideosForLastWeekFromAllFollowUsers(@Param("follower") User follower);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id inner join subscriptions s on u.id = s.follow_user where u.status = 'ACTIVE' and s.follower = :follower and v.user_id = s.follow_user and (lower(first_name) like %:search% or lower(last_name) like %:search%) and v.add_date >= (now() at time zone 'UTC' - interval '7 day')", nativeQuery = true)
    Iterable<Video> getAllVideosForLastWeekFromAllFollowUsersByFollowUserSearch(@Param("follower") User follower, @Param("search") String search);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id where u.status = 'ACTIVE' and v.id != :videoId order by random() limit 10", nativeQuery = true)
    Iterable<Video> findRandomVideosWithoutSelectedVideo(@Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "update videos set likes = :likes where id = :videoId", nativeQuery = true)
    void likeVideo(@Param("videoId") Long videoId, @Param("likes") Long likes);

    @Query(value = "select likes from videos where id = :videoId", nativeQuery = true)
    Long getLikesVideo(@Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "update videos set dislikes = :dislikes where id = :videoId", nativeQuery = true)
    void dislikeVideo(@Param("videoId") Long videoId, @Param("dislikes") Long dislikes);

    @Query(value = "select dislikes from videos where id = :videoId", nativeQuery = true)
    Long getDislikesVideo(@Param("videoId") Long videoId);
}
