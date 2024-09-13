package com.fthon.save_track.user.repository;

import com.fthon.save_track.event.persistence.Subscription;
import com.fthon.save_track.user.persistence.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);

    @Query("SELECT s FROM Subscription s " +
            "WHERE s.eventEntity.id = :eventId " +
            "AND s.userEntity.id = :userId " +
            "AND s.canceledAt > :currentTime")
    Optional<Subscription> findCurrentSubscription(@Param("userId")Long userId, @Param("eventId")Long eventId, @Param("currentTime")ZonedDateTime currentTime);
}