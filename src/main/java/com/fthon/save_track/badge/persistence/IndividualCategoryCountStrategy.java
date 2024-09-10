package com.fthon.save_track.badge.persistence;


import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class IndividualCategoryCountStrategy extends BadgeChallengeStrategy{

    private int targetCount;

    @ManyToOne
    private Category category;

    @Override
    public boolean check(User user) {
        return user.getLogs().stream()
                .filter((log)->category.equals(log.getEvent().getCategory()))
                .filter(UserEventLog::isChecked)
                .count() >= targetCount;
    }
}