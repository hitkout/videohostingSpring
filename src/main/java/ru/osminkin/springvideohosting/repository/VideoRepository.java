package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    Optional<Video> findById(Long id);
    Iterable<Video> findVideosById(Long id);
    Iterable<Video> findVideoByUserId(Long id);

    @Query(value = "select * from videos order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDateDesc();

    @Query(value = "select * from videos order by add_date", nativeQuery=true)
    Iterable<Video> findAllVideosOrderByDate();

    @Query(value = "select * from videos where user_id = :id order by add_date desc", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from videos where user_id = :id order by add_date", nativeQuery=true)
    Iterable<Video> findAllUserVideosOrderByDate(@Param("id") Long id);

    @Query(value = "select * from videos where id = :id", nativeQuery = true)
    Video findVideoById(@Param("id") Long id);

    @Query(value = "select * from videos order by random() limit 5", nativeQuery = true)
    List<Video> getFiveRandomVideos();

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id inner join subscriptions s on u.id = s.follow_user where follower = :follower and v.user_id = s.follow_user", nativeQuery = true)
    List<Video> getAllVideosFromAllFollowUsers(@Param("follower") User follower);

    @Query(value = "select * from videos v inner join users u on v.user_id = u.id inner join subscriptions s on u.id = s.follow_user where follower = :follower and v.user_id = s.follow_user and v.add_date >= (now() at time zone 'UTC' - interval '7 day')", nativeQuery = true)
    List<Video> getAllVideosForLastWeekFromAllFollowUsers(@Param("follower") User follower);

    @Query(value = "select * from videos where id != :videoId order by random() limit 10", nativeQuery = true)
    List<Video> findRandomVideosWithoutSelectedVideo(@Param("videoId") Long videoId);
}
