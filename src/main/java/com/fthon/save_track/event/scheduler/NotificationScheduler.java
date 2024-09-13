package com.fthon.save_track.event.scheduler;

import com.fthon.save_track.common.service.IOSPushService;
import com.fthon.save_track.user.dto.UserSubscriptionInfoDto;
import com.fthon.save_track.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {

    private final UserService userService;
    private final IOSPushService pushService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendMorningNotifications(){
        List<UserSubscriptionInfoDto> subscriptions = userService.getCurrentSubscribes(ZonedDateTime.now());
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        subscriptions.forEach(user->
                user.getSubscribeEvents().stream().filter(e->e.getNotificationDayOfWeeks().contains(dayOfWeek)).forEach(e->
                        pushService.sendNotification(user.getUserDeviceToken(), String.format("%s에서 보낸 메시지입니다! %s", e.getEventName(), e.getAfternoonMessage()))
                )
        );
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendAfternoonNotifications(){
        List<UserSubscriptionInfoDto> subscriptions = userService.getCurrentSubscribes(ZonedDateTime.now());

        subscriptions.forEach(user->
                user.getSubscribeEvents().forEach(e->
                        pushService.sendNotification(user.getUserDeviceToken(), String.format("%s에서 보낸 메시지입니다! %s", e.getEventName(), e.getEveningMessage()))
                )
        );
    }
    @Scheduled(cron = "0 0 18 * * ?")
    public void sendEveningNotifications(){
        List<UserSubscriptionInfoDto> subscriptions = userService.getCurrentSubscribes(ZonedDateTime.now());

        subscriptions.forEach(user->
                user.getSubscribeEvents().forEach(e->
                        pushService.sendNotification(user.getUserDeviceToken(), String.format("%s에서 보낸 메시지입니다! %s", e.getEventName(), e.getMorningMessage()))
                )
        );
    }
}
