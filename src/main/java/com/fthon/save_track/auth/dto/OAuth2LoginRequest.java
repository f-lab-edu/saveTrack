package com.fthon.save_track.auth.dto;


import lombok.Getter;

@Getter
public class OAuth2LoginRequest {

    private String provider;
    private String accessToken;

}
