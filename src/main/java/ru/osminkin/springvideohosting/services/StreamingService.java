package ru.osminkin.springvideohosting.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamingService {
    @Value("${upload.path.video}")
    private String uploadPathVideos;

    private final ResourceLoader resourceLoader;

    public StreamingService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> getVideo(String title) {
        final String FORMAT = "file:/" + uploadPathVideos + "/%s.mp4";
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(FORMAT, title)));
    }
}
