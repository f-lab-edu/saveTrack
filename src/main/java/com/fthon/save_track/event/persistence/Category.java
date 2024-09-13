package com.fthon.save_track.event.persistence;


import com.fthon.save_track.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(unique = true)
    private String uid;

    private String name;

    @Builder
    public Category(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
