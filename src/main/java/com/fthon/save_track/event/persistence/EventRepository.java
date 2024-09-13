package com.fthon.save_track.event.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.deleted = true WHERE e.id = :eventId")
    void softDeleteEvent(@Param("eventId") Long eventId);

    Page<Event> findAll(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.category.id = :categoryId")
    Page<Event> findByCategoryId(Pageable pageable, @Param("categoryId") Long categoryId);

}
