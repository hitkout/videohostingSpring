package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Iterable<Message> findAllMessages(){
        return messageRepository.findAll();
    }

    public Iterable<Message> findAllUserMessages(Long id){
        return messageRepository.findMessagesByUserId(id);
    }

    public void save(Message message){
        messageRepository.save(message);
    }

    public void deleteMessageById(Long id){
        messageRepository.deleteById(id);
    }
}
