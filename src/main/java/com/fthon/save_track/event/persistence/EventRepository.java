package com.fthon.save_track.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.deleted = true WHERE e.id = :eventId")
    void softDeleteEvent(@Param("eventId") Long eventId);
}
