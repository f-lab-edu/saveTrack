package com.fthon.save_track.common.service;

public interface IOSPushService {
    void sendNotification(String deviceToken, String message);
}
