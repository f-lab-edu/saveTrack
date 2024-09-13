package com.fthon.save_track.user.dto;


import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
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
        @Builder.Default
        private final List<DayOfWeek> notificationDayOfWeeks = List.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        );

        public static SubscribeEventInfoDto of(Subscription subscription){
            Event event = subscription.getEventEntity();
            return SubscribeEventInfoDto.builder()
                    .eventName(event.getEventName())
                    .morningMessage(event.getMorningCheerMessage())
                    .afternoonMessage(event.getAfternoonCheerMessage())
                    .eveningMessage(event.getEveningCheerMessage())
                    .notificationDayOfWeeks(event.getDaysOfWeek())
                    .build();
        }
    }

}
