package com.fthon.save_track.user.repository;

import com.fthon.save_track.user.persistence.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String email);
}