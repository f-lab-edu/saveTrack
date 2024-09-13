package com.fthon.save_track.user.persistence;

import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.persistence.Subscription;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class UserEventLog extends BaseEntity {

    @ManyToOne
    private Subscription subscription;

    private boolean isChecked;

}