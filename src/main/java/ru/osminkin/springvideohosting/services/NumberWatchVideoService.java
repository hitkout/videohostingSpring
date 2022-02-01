package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.repository.NumberWatchVideoRepository;

@Service
public class NumberWatchVideoService {
    private final NumberWatchVideoRepository numberWatchVideoRepository;

    public NumberWatchVideoService(NumberWatchVideoRepository numberWatchVideoRepository) {
        this.numberWatchVideoRepository = numberWatchVideoRepository;
    }

    public void watch(Long videoId){
        if (numberWatchVideoRepository.isWatch(videoId)){
            numberWatchVideoRepository.watch(videoId);
        }else numberWatchVideoRepository.createRecordWatch(videoId);
    }

    public Long getNumber(Long videoId){
        return numberWatchVideoRepository.getNumber(videoId);
    }
}
