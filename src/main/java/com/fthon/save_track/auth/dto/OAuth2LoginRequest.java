package com.fthon.save_track.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2LoginRequest {

    private String provider;
    private String accessToken;

}