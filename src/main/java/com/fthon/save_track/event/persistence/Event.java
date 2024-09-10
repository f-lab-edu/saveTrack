package com.fthon.save_track.event.persistence;

import com.fthon.save_track.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event extends BaseEntity {


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private List<Category> category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private List<Subscription> subscribeEntity;



    private String eventName;
    private String eventContent;
    private String morningCheerMessage;
    private String afternoonCheerMessage;
    private String eveningCheerMessage;
}