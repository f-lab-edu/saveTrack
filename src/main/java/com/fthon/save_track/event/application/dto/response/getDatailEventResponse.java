package com.fthon.save_track.event.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class getDatailEventResponse {
    private String eventName;
    private String eventContent;
}