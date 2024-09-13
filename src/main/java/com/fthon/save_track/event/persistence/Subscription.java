package com.fthon.save_track.event.persistence;

import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subscription")
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event eventEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userEntity;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "subscription")
    @Builder.Default
    private List<UserEventLog> logs = new ArrayList<>();

    private boolean eventCheck; // 지워야 할거같아요

    @Builder.Default
    private ZonedDateTime subscribedAt = ZonedDateTime.now();

    @Builder.Default
    private ZonedDateTime canceledAt = ZonedDateTime.of(LocalDateTime.MAX, ZoneId.systemDefault());


    public UserEventLog addLog(boolean check){
        UserEventLog log = new UserEventLog(this, check);
        logs.add(log);
        return log;
    }

}
