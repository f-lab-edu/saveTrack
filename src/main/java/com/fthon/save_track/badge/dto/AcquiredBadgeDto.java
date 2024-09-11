package com.fthon.save_track.badge.dto;


import com.fthon.save_track.badge.persistence.Badge;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AcquiredBadgeDto {

    private final Long badgeId;
    private final String badgeName;

    public static AcquiredBadgeDto of(Badge badge){
        return new AcquiredBadgeDto(badge.getId(), badge.getName());
    }

}