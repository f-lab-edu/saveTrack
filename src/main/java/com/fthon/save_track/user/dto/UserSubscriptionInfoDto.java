package com.fthon.save_track.user.dto;


import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserSubscriptionInfoDto {

    private final String userDeviceToken;

    private final List<SubscribeEventInfoDto> subscribeEvents;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SubscribeEventInfoDto{
        private final String eventName;
        private final String morningMessage;
        private final String afternoonMessage;
        private final String eveningMessage;

        public static SubscribeEventInfoDto of(Subscription subscription){
            Event event = subscription.getEventEntity();
            return SubscribeEventInfoDto.builder()
                    .eventName(event.getEventName())
                    .morningMessage(event.getMorningCheerMessage())
                    .afternoonMessage(event.getAfternoonCheerMessage())
                    .eveningMessage(event.getEveningCheerMessage())
                    .build();
        }
    }

}
