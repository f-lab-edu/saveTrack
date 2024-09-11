package com.fthon.save_track.user.service;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.dto.KakaoUserInfo;
import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.client.KakaoOAuth2Client;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.common.exceptions.UnAuthorizedException;
import com.fthon.save_track.user.dto.UserEventLogDto;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

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
                        user.getId(),
                        user.getEmail(),
                        user.getNickname()
                ),
                new Date()
        );
    }

    public List<UserEventLogDto> getEventLogDateIn(Long userId, LocalDate startDate, LocalDate endDate){
        User user = userRepository.findById(userId).orElseThrow(() -> new UnAuthorizedException("사용자 정보를 조회할 수 없습니다."));

        return user.getLogs().stream().filter(log->
                {
                    ZonedDateTime startDateTime = ZonedDateTime.of(startDate, LocalTime.MIN, log.getCreatedAt().getZone());
                    ZonedDateTime endDateTime = ZonedDateTime.of(endDate, LocalTime.MIN, log.getCreatedAt().getZone());
                    return
                            isEqualOrAfter(log.getCreatedAt(), startDateTime) &&
                            isEqualOrBefore(log.getCreatedAt(), endDateTime);
                }
        ).map(UserEventLogDto::of).toList();
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

    private boolean isEqualOrAfter(ZonedDateTime date1, ZonedDateTime date2) {
        return date1.isEqual(date2) || date1.isAfter(date2);
    }
    private boolean isEqualOrBefore(ZonedDateTime date1, ZonedDateTime date2) {
        return date1.isEqual(date2) || date1.isBefore(date2);
    }

}