package com.fthon.save_track.badge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BadgeSearchResponse {

    private Long badgeId;
    private String badgeName;
    private boolean acquired;
    private ZonedDateTime acquiredAt;
}