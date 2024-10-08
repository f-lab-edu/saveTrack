package com.fthon.save_track.user.persistence;


import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    private String nickname;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String email;

    private String deviceToken;
    public User(String nickname, Long kakaoId, String email, String deviceToken) {
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.email = email;
        this.deviceToken = deviceToken;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntity")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserBadge> badges = new ArrayList<>();

    public Subscription addSubscription(Event event){
        Subscription subscription = Subscription.builder()
                .eventEntity(event)
                .userEntity(this)
                .build();

        this.subscriptions.add(subscription);
        return subscription;
    }

    public void addBadge(Badge badge){
        this.badges.add(new UserBadge(this, badge));
    }

}