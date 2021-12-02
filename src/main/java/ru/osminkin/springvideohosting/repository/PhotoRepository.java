package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osminkin.springvideohosting.model.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Override
    Optional<Photo> findById(Long id);
    Iterable<Photo> findPhotosByUserId(Long id);
}
