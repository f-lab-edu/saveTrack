package com.fthon.save_track.badge.repository;

import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.user.persistence.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BadgeRepository extends CrudRepository<Badge, Long> {


    @Query("SELECT b From Badge b " +
            "WHERE b.id not in (SELECT ub.badge.id FROM UserBadge ub WHERE ub.user = :user)")
    List<Badge> findBadgesNotAcquired(@Param("user") User user);

}