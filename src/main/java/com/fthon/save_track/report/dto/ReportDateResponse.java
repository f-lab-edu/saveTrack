package com.fthon.save_track.report.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportDateResponse {

    private String eventId;
    private String eventName;
    private boolean isCheck;
    private ZonedDateTime checkTime;

}