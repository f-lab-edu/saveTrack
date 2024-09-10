package com.fthon.save_track.user.persistence;


import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.persistence.Event;
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


    public User(String nickname, Long kakaoId, String email) {
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.email = email;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserEventLog> logs = new ArrayList<>();

    public void addLog(Event event, boolean isCheck){
        this.logs.add(new UserEventLog(this, event, isCheck));
    }

}