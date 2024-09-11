package com.fthon.save_track.report.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportDateResponse {

    private Long eventId;
    private String eventName;
    private boolean checked;
    private ZonedDateTime checkTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDateResponse that = (ReportDateResponse) o;
        return checked == that.checked && Objects.equals(eventId, that.eventId) && Objects.equals(eventName, that.eventName) && checkTime.isEqual(that.checkTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventName, checked, checkTime);
    }
}