package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Override
    Optional<Photo> findById(Long id);
    Iterable<Photo> findPhotosByUserId(Long id);

    @Query(value = "select * from photos order by id desc limit 7", nativeQuery = true)
    Iterable<Photo> findLast10();

    Iterable<Photo> findAllPhotosByIdBetweenOrderByIdDesc(Long start, Long finish);

    @Query(value = "select MIN(id) from photos", nativeQuery = true)
    Long getLastId();

    @Query(value = "select * from photos order by add_date desc", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDate();

    @Query(value = "select * from photos order by add_date", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDateDesc();

    @Query(value = "select * from photos where user_id = :id order by add_date desc", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDate(@Param("id") Long id);

    @Query(value = "select * from photos where user_id = :id order by add_date", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from photos where id = :id", nativeQuery = true)
    Photo findPhotoById(@Param("id") Long id);
}
