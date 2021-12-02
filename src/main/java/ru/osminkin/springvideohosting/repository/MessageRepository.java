package ru.osminkin.springvideohosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.osminkin.springvideohosting.model.Message;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Override
    Optional<Message> findById(Long id);
    Iterable<Message> findMessagesByUserId(Long id);
}
