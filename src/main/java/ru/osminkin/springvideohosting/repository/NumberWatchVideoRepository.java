package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.NumberWatchVideo;
import ru.osminkin.springvideohosting.model.User;

import javax.transaction.Transactional;

public interface NumberWatchVideoRepository extends JpaRepository<NumberWatchVideo, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into number_watch_video(number, video_id) values (1, :videoId)", nativeQuery = true)
    void createRecordWatch(@Param("videoId") Long videoId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update number_watch_video set number = number + 1 where video_id = :videoId", nativeQuery = true)
    void watch(@Param("videoId") Long videoId);

    @Query(value = "select exists(select from number_watch_video where video_id = :videoId)", nativeQuery = true)
    boolean isWatch(@Param("videoId") Long videoId);

    @Query(value = "select number from number_watch_video where video_id = :videoId", nativeQuery = true)
    Long getNumber(@Param("videoId") Long videoId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from number_watch_video where video_id = :videoId", nativeQuery = true)
    void deleteByVideoId(@Param("videoId") Long videoId);
}
