package com.fthon.save_track.event.persistence;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.application.dto.request.EventUpdateRequest;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event")
public class Event extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private List<Subscription> subscribeEntity;

    private String eventName;
    private String eventContent;
    private String morningCheerMessage;
    private String afternoonCheerMessage;
    private String eveningCheerMessage;

    public Event update(EventUpdateRequest event) {
        eventName = event.getEventName();
        eventContent = event.getEventContent();
        return this;
    }
}