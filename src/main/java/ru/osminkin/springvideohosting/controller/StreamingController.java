package ru.osminkin.springvideohosting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.osminkin.springvideohosting.services.StreamingService;

@RestController
public class StreamingController {
    @Autowired
    private StreamingService streamingService;

    @GetMapping(value = "/auth/success/channel/{userId}/video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable long userId,
                                   @PathVariable String title,
                                   @RequestHeader("Range") String range) {
        System.out.println(range);
        return streamingService.getVideo(title);
    }
}
