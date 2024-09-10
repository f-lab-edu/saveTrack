package com.fthon.save_track.badge.persistence;

import com.fthon.save_track.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "badges")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Badge extends BaseEntity {

    private String name;


    @OneToOne(cascade = {CascadeType.PERSIST})
    private BadgeChallengeStrategy strategy;
}