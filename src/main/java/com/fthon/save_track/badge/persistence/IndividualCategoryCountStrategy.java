package com.fthon.save_track.badge.persistence;


import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.user.persistence.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class IndividualCategoryCountStrategy extends BadgeChallengeStrategy{

    private int targetCount;

    @ManyToOne
    private Category category;

    @Override
    public boolean check(User user) {
        return false;
    }
}