package com.fthon.save_track.common.controller;


import com.fthon.save_track.common.service.IOSPushService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DevelopmentController {

    private final IOSPushService iosPushService;

    @PostMapping("/api/test")
    public void testSendNotification(
            @RequestBody NotificationRequest req
    ){
        iosPushService.sendNotification(req.getDeviceToken(), req.getPayload());
    }


    @Getter
    public static class NotificationRequest{
        private String deviceToken;
        private String payload;

    }
}
