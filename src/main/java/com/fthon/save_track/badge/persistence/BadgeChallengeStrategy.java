package com.fthon.save_track.badge.persistence;


import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.user.persistence.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class BadgeChallengeStrategy extends BaseEntity {

    public abstract boolean check(User user);
}