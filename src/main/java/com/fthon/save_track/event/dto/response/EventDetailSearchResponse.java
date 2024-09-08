package com.fthon.save_track.event.dto.response;

import com.fthon.save_track.common.consts.TimeOfDay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventDetailSearchResponse {

    private String eventId;
    private String name;
    private String purpose;
    private List<DayOfWeek> dayOfWeeks;
    private Integer joinCount;
    private String categoryName;
    private List<CheerMessage> cheerMessages;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CheerMessage{
        private TimeOfDay timeOfDay;
        private String cheerMessage;

    }
}