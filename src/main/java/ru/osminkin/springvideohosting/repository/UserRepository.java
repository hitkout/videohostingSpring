package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.osminkin.springvideohosting.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    User findUserByEmail(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set status = 'BANNED' where id = :bannedUser", nativeQuery = true)
    void banUserById(@Param("bannedUser") Long bannedUser);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set status = 'ACTIVE' where id = :bannedUser", nativeQuery = true)
    void unbanUserById(@Param("bannedUser") Long bannedUser);

    @Query(value = "select role from users where id = :authUser", nativeQuery = true)
    String getUserRole(@Param("authUser") User authUser);

    @Query(value = "select status from users where id = :userId", nativeQuery = true)
    String getUserStatus(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set photo = :photoName where id = :id", nativeQuery = true)
    void updatePhoto(@Param("id") Long id, @Param("photoName") String photoName);

    @Query(value = "select * from users where lower(first_name) like %:search% or lower(last_name) like %:search%", nativeQuery = true)
    List<User> findBySearch(@Param("search") String search);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into subscriptions(follower, follow_user) values (:follower, :followUser)",nativeQuery = true)
    void subscribe(@Param("follower") User follower, @Param("followUser") User followUser);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from subscriptions where follower = :follower and follow_user = :followUser",nativeQuery = true)
    void unsubscribe(@Param("follower") User follower, @Param("followUser") User followUser);

    @Query(value = "select exists(select from subscriptions where follower = :follower and follow_user = :followUser)", nativeQuery = true)
    boolean isSubscribe(@Param("follower") User follower, @Param("followUser") User followUser);

    @Query(value = "select * from users u inner join subscriptions s on u.id = s.follow_user where follower = :follower", nativeQuery = true)
    List<User> findAllUserByUserAuthFromSubscriptions(@Param("follower") User follower);

    @Query(value = "select * from users u inner join subscriptions s on u.id = s.follow_user where follower = :follower and (lower(first_name) like %:search% or lower(last_name) like %:search%)", nativeQuery = true)
    List<User> findBySearchUserAuthFromSubscriptions(@Param("follower") User follower, @Param("search") String search);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set first_name = :name where id = :id", nativeQuery = true)
    void changeName(@Param("id") Long id, @Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set last_name = :surname where id = :id", nativeQuery = true)
    void changeSurname(@Param("id") Long id, @Param("surname") String surname);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update users set password = :password where id = :id", nativeQuery = true)
    void changeUserPassword(@Param("id") Long id, @Param("password") String password);

    @Query(value = "select count(follower) from subscriptions where follow_user = :user", nativeQuery = true)
    Long getSubscribersCount(@Param("user") User user);
}