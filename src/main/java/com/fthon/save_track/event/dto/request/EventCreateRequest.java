package com.fthon.save_track.event.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventCreateRequest {

    private String name;
    private String purpose;
    private List<DayOfWeek> dayOfWeeks;
    private String categoryId;
    private String morningCheerMessage;
    private String afternoonCheerMessage;
    private String eveningCheerMessage;

}