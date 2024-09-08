package com.fthon.save_track.badge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BadgeSearchResponse {

    private String badgeId;
    private String badgeName;
    private ZonedDateTime acquiredAt;
}