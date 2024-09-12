package com.fthon.save_track.user.dto;


import com.fthon.save_track.user.persistence.UserEventLog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEventLogDto {

    private final Long eventId;
    private final String eventName;
    private boolean isCheck;
    private ZonedDateTime checkTime;

    public static UserEventLogDto of(UserEventLog log){
        return new UserEventLogDto(
                log.getEvent().getId(),
                log.getEvent().getEventName(),
                log.isChecked(),
                log.getCreatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEventLogDto that = (UserEventLogDto) o;
        return isCheck == that.isCheck && Objects.equals(eventId, that.eventId) && Objects.equals(eventName, that.eventName) && Objects.equals(checkTime, that.checkTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, isCheck, checkTime);
    }
}