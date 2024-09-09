package com.fthon.save_track.auth.service.client;


import com.fthon.save_track.auth.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOAuth2Client {

    private final String kakaoOAuth2InfoUrl;

    private final String kakaoOauth2AdminKey;

    private final RestClient restClient;

    public KakaoOAuth2Client(
            @Value("${service.oauth2.kakao.info-url}") String kakaoOAuth2InfoUrl,
            @Value("${service.oauth2.kakao.admin-key}") String kakaoOauth2AdminKey,
            RestClient restClient
            ) {
        this.kakaoOAuth2InfoUrl = kakaoOAuth2InfoUrl;
        this.kakaoOauth2AdminKey = kakaoOauth2AdminKey;
        this.restClient = restClient;
    }

    public KakaoUserInfo getUserInfo(String token){
        return restClient.get()
                .uri(kakaoOAuth2InfoUrl)
                .header("Authorization", "Bearer " + kakaoOauth2AdminKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(KakaoUserInfo.class);
    }


}