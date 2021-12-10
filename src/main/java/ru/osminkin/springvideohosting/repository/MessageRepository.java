package ru.osminkin.springvideohosting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.User;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Override
    Optional<Message> findById(Long id);
    Iterable<Message> findMessagesByUserId(Long id);

    List<Message> findAll(Sort sort);

    @Query(value = "select * from posts order by id desc limit 1", nativeQuery = true)
    Iterable<Message> findLast10();

    Iterable<Message> findAllByIdBetweenOrderByIdDesc(Long start, Long finish);
}
