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
                        user.getId().toString(),
                        user.getEmail(),
                        user.getNickname()
                ),
                new Date()
        );
    }


    @Transactional(readOnly = true)
    public String loginByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new RuntimeException("유저를 조회할 수 없습니다.");
        }

        return jwtUtils.sign(AuthenticatedUserDto.of(user.get()), new Date());
    }


    private User saveProcess(KakaoUserInfo kakaoUserInfo){
        User user = new User(
                kakaoUserInfo.getKakaoAccount().getName(),
                kakaoUserInfo.getId(),
                kakaoUserInfo.getKakaoAccount().getEmail()
        );

        userRepository.save(user);
        return user;
    }
}