package com.fthon.save_track.auth.service;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.dto.KakaoUserInfo;
import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.client.KakaoOAuth2Client;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;


    /**
     * 카카오 토큰을 입력받아 DB에 사용자 정보를 필요시 저장하고 JWT를 반환한다.
     * @author minseok kim
     * @return JWT Token
    */
    public String doOAuth2Login(OAuth2LoginRequest loginRequest){

        KakaoUserInfo result = kakaoOAuth2Client.getUserInfo(loginRequest.getAccessToken());

        Optional<User> savedUser = userRepository.findByKakaoId(result.getId());

        User user = savedUser.orElseGet(()->saveProcess(result));

        return jwtUtils.sign(
                new AuthenticatedUserDto(
                        user.getUid(),
                        user.getEmail(),
                        user.getNickname()
                ),
                new Date()
        );
    }



    private User saveProcess(KakaoUserInfo kakaoUserInfo){
        User user = new User(
                kakaoUserInfo.getKakaoAccount().getName(),
                UUID.randomUUID().toString(),
                kakaoUserInfo.getId(),
                kakaoUserInfo.getKakaoAccount().getEmail()
        );

        userRepository.save(user);
        return user;
    }
}