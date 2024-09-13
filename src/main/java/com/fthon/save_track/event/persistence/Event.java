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

import java.time.DayOfWeek;
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

    @Builder.Default
    private boolean deleted = false;

    private String eventName;
    private String eventContent;
    private String morningCheerMessage;
    private String afternoonCheerMessage;
    private String eveningCheerMessage;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "event_noti_day_of_week", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "noti_day_of_week")
    private List<DayOfWeek> daysOfWeek;

    
    public Event update(EventUpdateRequest event) {
        eventName = event.getEventName();
        eventContent = event.getEventContent();
        return this;
    }

    public void softDelete() {
        this.deleted = true;  // 삭제 상태로 변경
    }
}