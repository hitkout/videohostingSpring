package ru.osminkin.springvideohosting.services;

import org.springframework.stereotype.Service;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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

    public Iterable<Message> findAll(){
        return messageRepository.findAll();
    }

    public Iterable<Message> findAllMessagesOrderByDate(){
        return messageRepository.findAllMessagesOrderByDate();
    }

    public Iterable<Message> findAllMessagesOrderByDateDesc(){
        return messageRepository.findAllMessagesOrderByDateDesc();
    }

    public Iterable<Message> findAllUserMessagesOrderByDate(Long id){
        return messageRepository.findAllUserMessagesOrderByDate(id);
    }

    public Iterable<Message> findAllUserMessagesOrderByDateDesc(Long id){
        return messageRepository.findAllUserMessagesOrderByDateDesc(id);
    }
}
