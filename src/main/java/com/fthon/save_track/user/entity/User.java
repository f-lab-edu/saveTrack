package com.fthon.save_track.user.entity;


import com.fthon.save_track.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String nickname;

    @Column(unique = true)
    private String uid;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String email;


}