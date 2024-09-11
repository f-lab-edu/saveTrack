package com.fthon.save_track.user.persistence;

import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBadge extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Badge badge;

}