package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Message;
import ru.osminkin.springvideohosting.model.User;
import ru.osminkin.springvideohosting.repository.MessageRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public Iterable<Message> findAllMessages(){
        return messageRepository.findAll();
    }

    public void setMessageFromForm(User user, MultipartFile file, Message messageMessage) throws IOException {
        messageMessage.setUser(user);
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPath);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            messageMessage.setFilename(resultFilename);
        }
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
