package com.fthon.save_track.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUserDto {

    private final Long id;
    private final String email;
    private final String nickname;
}
