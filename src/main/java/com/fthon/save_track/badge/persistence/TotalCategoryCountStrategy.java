package com.fthon.save_track.badge.persistence;

import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalCategoryCountStrategy extends BadgeChallengeStrategy{

    private int targetCount;

    @Override
    public boolean check(User user) {
        return user.getSubscriptions().stream()
                .flatMap(s->s.getLogs().stream())
                .filter(UserEventLog::isChecked)
                .count() >= targetCount;
    }
}