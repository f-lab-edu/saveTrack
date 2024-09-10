package com.fthon.save_track.event.application.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventUpdateRequest {

    private String name;
    private String purpose;
    private List<DayOfWeek> dayOfWeeks;
    private String categoryId;
    private String morningCheerMessage;
    private String afternoonCheerMessage;
    private String eveningCheerMessage;
}