package com.fthon.save_track.auth.dto;


import com.fthon.save_track.user.persistence.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUserDto {

    private final Long id;
    private final String email;
    private final String nickname;


    public static AuthenticatedUserDto of(User user){
        return new AuthenticatedUserDto(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}