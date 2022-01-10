package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.osminkin.springvideohosting.model.Photo;
import ru.osminkin.springvideohosting.repository.PhotoRepository;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserService userService;
    @Value("${upload.path.image}")
    private String uploadPathImages;

    public void savePhotoInDb(long userId, MultipartFile file, Photo photoFromForm) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File upload = new File(uploadPathImages);
            if (!upload.exists()){
                upload.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPathImages + "/" + resultFilename));
            photoFromForm.setFilename(resultFilename);
            photoFromForm.setUser(userService.findUserById(userId));
            photoRepository.save(photoFromForm);
        }
    }

    public Iterable<Photo> findAllPhotosByUserId(Long id){
        return photoRepository.findPhotosByUserId(id);
    }

    public void deletePhotoById(Long id){
        photoRepository.deleteById(id);
    }
}
