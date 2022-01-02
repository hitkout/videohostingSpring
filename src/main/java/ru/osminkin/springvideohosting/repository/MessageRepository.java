package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.osminkin.springvideohosting.model.Message;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Override
    Optional<Message> findById(Long id);
    Iterable<Message> findMessagesByUserId(Long id);

    @Query(value = "select * from posts order by id desc limit 7", nativeQuery = true)
    Iterable<Message> findLast10();

    Iterable<Message> findAllByIdBetweenOrderByIdDesc(Long start, Long finish);

    @Query(value = "select * from posts order by add_date", nativeQuery = true)
    Iterable<Message> findAllOldMessages();
}
