package com.fthon.save_track.common.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.ApnsPushNotification;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.fthon.save_track.common.properties.APNProperty;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
@Slf4j
@Profile("!test")
public class IOSPushServiceImpl implements IOSPushService {

    private final APNProperty apnProperty;
    private ApnsClient client;

    public IOSPushServiceImpl(APNProperty apnProperty){
        this.apnProperty = apnProperty;
    }


    @Override
    public void sendNotification(String deviceToken, String message){
        final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setAlertBody(message);

        final String payload = payloadBuilder.build();
        final String token = TokenUtil.sanitizeTokenString(deviceToken);

        ApnsPushNotification pushNotification =
                new SimpleApnsPushNotification(token, apnProperty.getBundleId(), payload);

        client.sendNotification(pushNotification).whenComplete((response, throwable) -> {
            if(response == null || !response.isAccepted()){
                log.warn(throwable.toString());
                return;
            }

            log.info("Notification Successed.");
        });
    }



    @PostConstruct
    protected void init() {
        try {
            this.client = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                            new File(apnProperty.getP8FilePath()),
                            apnProperty.getTeamId(),
                            apnProperty.getKeyId()))
                    .build();
        } catch (Exception e) {
            // 예외를 다시 던져서 애플리케이션 시작 중단
            throw new IllegalStateException("APNs client 초기화에 실패했습니다.", e);
        }
    }

    @PreDestroy
    protected void destroy(){
        this.client.close();
    }



    @Component
    @Slf4j
    @Profile("test")
    public static class TestIos implements IOSPushService{

        @Override
        public void sendNotification(String deviceToken, String message) {

        }
    }
}

