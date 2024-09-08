package com.fthon.save_track.common.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

@MappedSuperclass
@NoArgsConstructor
@Getter
public class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @PrePersist
    protected void prePersist(){
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void preUpdate(){
        updatedAt = ZonedDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}