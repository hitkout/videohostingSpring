package ru.osminkin.springvideohosting.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;
import ru.osminkin.springvideohosting.repository.RecordRepository;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecordService {
    private final RecordRepository recordsRepository;

    public RecordService(RecordRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }

    public Iterable<Record> findAllUserRecords(Long id){
        return recordsRepository.findRecordsByUserId(id);
    }

    public void save(Record records){
        recordsRepository.save(records);
    }

    public void deleteRecordById(Long id){
        recordsRepository.deleteById(id);
    }

    public Iterable<Record> findAll(){
        return recordsRepository.findAll();
    }

    public Iterable<Record> findAllRecordsOrderByDate(){
        return recordsRepository.findAllRecordsOrderByDate();
    }

    public Iterable<Record> findAllRecordsOrderByDateDesc(){
        return recordsRepository.findAllRecordsOrderByDateDesc();
    }

    public Iterable<Record> findAllUserRecordsOrderByDate(Long id){
        return recordsRepository.findAllUserRecordsOrderByDate(id);
    }

    public Iterable<Record> findAllUserRecordsOrderByDateDesc(Long id){
        return recordsRepository.findAllUserRecordsOrderByDateDesc(id);
    }

    public List<Record> getFiveRandomRecords(){
        return recordsRepository.getFiveRandomRecords();
    }

    public void saveComment(User authUser, Video videoId, Record record) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        record.setAddDate(Timestamp.valueOf(dateFormat.format(GregorianCalendar.getInstance().getTime())));
        record.setUser(authUser);
        record.setVideoId(videoId);
        recordsRepository.save(record);
    }

    public List<Record> findAllCommentsByVideoId(Long videoId){
        return recordsRepository.findAllCommentsByVideoId(videoId);
    }
}
