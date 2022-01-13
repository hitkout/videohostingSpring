package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Video;

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
}
