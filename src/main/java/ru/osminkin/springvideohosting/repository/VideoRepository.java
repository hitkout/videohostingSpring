package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osminkin.springvideohosting.model.Video;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Override
    Optional<Video> findById(Long id);
    Iterable<Video> findVideosById(Long id);
}
