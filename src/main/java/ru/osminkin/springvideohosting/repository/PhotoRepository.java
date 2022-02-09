package ru.osminkin.springvideohosting.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Override
    @NonNull
    Optional<Photo> findById(@NonNull Long id);
    Iterable<Photo> findPhotosByUserId(Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE'", nativeQuery=true)
    Iterable<Photo> findAllPhotos();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDate();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDateDesc();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDate(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.id = :id", nativeQuery = true)
    Photo findPhotoById(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by random() limit 5", nativeQuery = true)
    List<Photo> getFiveRandomPhotos();
}
