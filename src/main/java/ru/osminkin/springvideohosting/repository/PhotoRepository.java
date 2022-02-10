package ru.osminkin.springvideohosting.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.model.Video;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Override
    @NonNull
    Optional<Photo> findById(@NonNull Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and u.id = :id", nativeQuery = true)
    Iterable<Photo> findPhotosByUserId(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and u.id = :id and (lower(text) like %:search%)", nativeQuery = true)
    Iterable<Photo> findPhotosByUserId(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE'", nativeQuery=true)
    Iterable<Photo> findAllPhotos();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDate();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and (lower(p.text) like %:search%) order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDate(@Param("search") String search);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDateDesc();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and (lower(p.text) like %:search%) order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllPhotosOrderByDateDesc(@Param("search") String search);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDateDesc(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id and (lower(text) like %:search%) order by p.add_date desc", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDateDesc(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDate(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.user_id = :id and (lower(text) like %:search%) order by p.add_date", nativeQuery=true)
    Iterable<Photo> findAllUserPhotosOrderByDate(@Param("id") Long id, @Param("search") String search);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' and p.id = :id", nativeQuery = true)
    Photo findPhotoById(@Param("id") Long id);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id where u.status = 'ACTIVE' order by random() limit 5", nativeQuery = true)
    Iterable<Photo> getFiveRandomPhotos();

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id inner join subscriptions s on u.id = s.follow_user where u.status = 'ACTIVE' and s.follower = :follower and p.user_id = s.follow_user and p.add_date >= (now() - interval '7 day')", nativeQuery = true)
    Iterable<Photo> getAllPhotosForLastWeekFromAllFollowUsers(@Param("follower") User follower);

    @Query(value = "select * from photos p inner join users u on p.user_id = u.id inner join subscriptions s on u.id = s.follow_user where u.status = 'ACTIVE' and s.follower = :follower and p.user_id = s.follow_user and (lower(first_name) like %:search% or lower(last_name) like %:search%) and p.add_date >= (now() - interval '7 day')", nativeQuery = true)
    Iterable<Photo> getAllPhotosForLastWeekFromAllFollowUsersByFollowUserSearch(@Param("follower") User follower, @Param("search") String search);
}
