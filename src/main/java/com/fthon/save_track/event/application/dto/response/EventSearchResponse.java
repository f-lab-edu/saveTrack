package com.fthon.save_track.event.application.dto.response;


import com.fthon.save_track.event.persistence.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventSearchResponse {

    private Long eventId;
    private String name;
    private String purpose;
    private List<DayOfWeek> dayOfWeeks;
    private Integer joinCount;

    public static EventSearchResponse of(Event e){
        return new EventSearchResponse(
                e.getId(),
                e.getEventName(),
                e.getEventContent(),
                e.getDaysOfWeek(),
                e.getSubscribeEntity().size()
        );
    }


}