package com.fthon.save_track.auth.service.client;


import com.fthon.save_track.auth.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOAuth2Client {

    private final String kakaoOAuth2InfoUrl;

    private final RestClient restClient;

    public KakaoOAuth2Client(
            @Value("${service.oauth2.kakao.info-url}") String kakaoOAuth2InfoUrl,
            RestClient restClient
            ) {
        this.kakaoOAuth2InfoUrl = kakaoOAuth2InfoUrl;
        this.restClient = restClient;
    }

    public KakaoUserInfo getUserInfo(String token){
        return restClient.get()
                .uri(kakaoOAuth2InfoUrl)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(KakaoUserInfo.class);
    }


}