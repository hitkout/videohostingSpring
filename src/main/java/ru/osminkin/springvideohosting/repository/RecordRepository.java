package ru.osminkin.springvideohosting.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Record;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @NonNull Optional<Record> findById(@NonNull Long id);
    Iterable<Record> findRecordsByUserId(Long id);

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' order by add_date desc", nativeQuery=true)
    Iterable<Record> findAllRecordsOrderByDate();

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' order by add_date", nativeQuery=true)
    Iterable<Record> findAllRecordsOrderByDateDesc();

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' and c.user_id = :id order by c.add_date desc", nativeQuery=true)
    Iterable<Record> findAllUserRecordsOrderByDate(@Param("id") Long id);

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' and c.user_id = :id order by c.add_date", nativeQuery=true)
    Iterable<Record> findAllUserRecordsOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' order by random() limit 5", nativeQuery = true)
    List<Record> getFiveRandomRecords();
    
    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' and c.video_id = :videoId", nativeQuery = true)
    List<Record> findAllCommentsByVideoId(@Param("videoId") Long videoId);

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' and c.video_id = :videoId order by c.add_date", nativeQuery = true)
    List<Record> findAllCommentsByNewVideoId(@Param("videoId") Long videoId);

    @Query(value = "select * from comments c inner join users u on c.user_id = u.id where u.status = 'ACTIVE' and c.video_id = :videoId order by c.add_date desc", nativeQuery = true)
    List<Record> findAllCommentsByOldVideoId(@Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query(value = "delete from comments where video_id = :videoId", nativeQuery = true)
    void deleteByVideoId(@Param("videoId") Long videoId);
}
