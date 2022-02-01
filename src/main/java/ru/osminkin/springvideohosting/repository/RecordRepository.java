package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.model.Video;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findById(Long id);
    Iterable<Record> findRecordsByUserId(Long id);

    @Query(value = "select * from comments order by id desc limit 7", nativeQuery = true)
    Iterable<Record> findLast10();

    Iterable<Record> findAllByIdBetweenOrderByIdDesc(Long start, Long finish);

    @Query(value = "select * from comments order by id", nativeQuery = true)
    Iterable<Record> findAllOldRecords();

    @Query(value = "select * from comments order by add_date desc", nativeQuery=true)
    Iterable<Record> findAllRecordsOrderByDate();

    @Query(value = "select * from comments order by add_date", nativeQuery=true)
    Iterable<Record> findAllRecordsOrderByDateDesc();

    @Query(value = "select * from comments where user_id = :id order by add_date desc", nativeQuery=true)
    Iterable<Record> findAllUserRecordsOrderByDate(@Param("id") Long id);

    @Query(value = "select * from comments where user_id = :id order by add_date", nativeQuery=true)
    Iterable<Record> findAllUserRecordsOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from comments order by random() limit 5", nativeQuery = true)
    List<Record> getFiveRandomRecords();
    
    @Query(value = "select * from comments where video_id = :videoId", nativeQuery = true)
    List<Record> findAllCommentsByVideoId(@Param("videoId") Long videoId);
}
