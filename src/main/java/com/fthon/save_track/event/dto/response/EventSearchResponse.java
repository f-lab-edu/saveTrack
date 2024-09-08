package com.fthon.save_track.event.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventSearchResponse {

    private String eventId;
    private String name;
    private String purpose;
    private List<DayOfWeek> dayOfWeeks;
    private Integer joinCount;

}