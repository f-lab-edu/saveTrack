package com.fthon.save_track.user.persistence;

import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.persistence.Subscription;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class UserEventLog extends BaseEntity {

    @ManyToOne
    private Subscription subscription;

    @Builder.Default
    private boolean isChecked = false;

    public void finish() {
        this.isChecked = true;  // 삭제 상태로 변경
    }

}