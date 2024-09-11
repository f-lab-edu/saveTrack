package com.fthon.save_track.auth.service;

import com.fthon.save_track.auth.dto.KakaoUserInfo;
import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.client.KakaoOAuth2Client;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private KakaoOAuth2Client kakaoOAuth2Client;


    @Test
    @DisplayName("DB에 저장되지 않은 사용자의 카카오 토큰을 전달받아 DB에 사용자 정보를 저장하고 JWT를 반환한다.")
    public void testKakaoLogin() throws Exception{
        //given
        KakaoUserInfo kakaoUserInfo = createDummyKakaoUserInfo();
        given(kakaoOAuth2Client.getUserInfo(any())).willReturn(kakaoUserInfo);


        //when
        OAuth2LoginRequest reqBody = new OAuth2LoginRequest(
                "kakao",
                "token"
        );

        String result = authService.doOAuth2Login(reqBody);

        //then
        assertThat(result).isNotNull();
        assertThat(userRepository.findByKakaoId(kakaoUserInfo.getId()).isPresent()).isTrue();
    }


    @Test
    @DisplayName("DB에 저장된 사용자의 카카오 토큰을 전달받아 JWT를 반환한다.")
    public void testSavedKakaoLogin() throws Exception{
        //given
        KakaoUserInfo kakaoUserInfo = createDummyKakaoUserInfo();
        given(kakaoOAuth2Client.getUserInfo(any())).willReturn(kakaoUserInfo);

        User user = new User(
                kakaoUserInfo.getKakaoAccount().getName(),
                kakaoUserInfo.getId(),
                kakaoUserInfo.getKakaoAccount().getEmail()
        );
        userRepository.save(user);

        //when
        OAuth2LoginRequest reqBody = new OAuth2LoginRequest(
                "kakao",
                "token"
        );

        String result = authService.doOAuth2Login(reqBody);

        //then
        assertThat(result).isNotNull();
    }

    private KakaoUserInfo createDummyKakaoUserInfo(){
        return  KakaoUserInfo.builder()
                .id(123456789L)
                .connectedAt("2022-04-11T01:45:28Z")
                .kakaoAccount(
                        KakaoUserInfo.KakaoAccount.builder()
                                .profile(
                                        KakaoUserInfo.KakaoAccount.Profile.builder()
                                                .nickname("홍길동")
                                                .thumbnailImageUrl("http://yyy.kakao.com/.../img_110x110.jpg")
                                                .profileImageUrl("http://yyy.kakao.com/dn/.../img_640x640.jpg")
                                                .build()
                                )
                                .name("홍길동")
                                .email("sample@sample.com")
                                .ageRange("20~29")
                                .birthyear("2002")
                                .birthday("1130")
                                .gender("female")
                                .phoneNumber("+82 010-1234-5678")
                                .build()
                )
                .build();

    }

}