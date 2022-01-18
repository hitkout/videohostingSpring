package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.Record;
import ru.osminkin.springvideohosting.repository.RecordRepository;

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
}
