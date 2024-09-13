package com.fthon.save_track.user.service;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.dto.KakaoUserInfo;
import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.client.KakaoOAuth2Client;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.common.exceptions.UnAuthorizedException;
import com.fthon.save_track.event.persistence.Subscription;
import com.fthon.save_track.user.dto.UserEventLogDto;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

        User user = savedUser.orElseGet(()->saveProcess(result, loginRequest.getDeviceToken()));

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

        return user.getSubscriptions().stream().flatMap(s->getUserEventLogStream(s, endDate)).filter(log->
                {
                    ZoneId zoneId = log.getCheckTime() == null ? ZoneId.systemDefault() : log.getCheckTime().getZone();

                    ZonedDateTime startDateTime = ZonedDateTime.of(startDate, LocalTime.MIN, zoneId);
                    ZonedDateTime endDateTime = ZonedDateTime.of(endDate, LocalTime.MAX, zoneId);
                    return
                            isEqualOrBefore(startDateTime, log.getCheckTime()) &&
                            isEqualOrAfter(endDateTime, log.getCheckTime());
                }
        ).toList();
    }


    @Transactional(readOnly = true)
    public String loginByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new UnAuthorizedException("유저를 조회할 수 없습니다.");
        }

        return jwtUtils.sign(AuthenticatedUserDto.of(user.get()), new Date());
    }


    private User saveProcess(KakaoUserInfo kakaoUserInfo, String deviceToken){
        User user = new User(
                kakaoUserInfo.getKakaoAccount().getName(),
                kakaoUserInfo.getId(),
                kakaoUserInfo.getKakaoAccount().getEmail(),
                deviceToken
        );

        userRepository.save(user);
        return user;
    }

    private boolean isEqualOrAfter(ZonedDateTime date1, ZonedDateTime date2) {
        if(date2 == null){
            return false;
        }
        return date1.isEqual(date2) || date1.isAfter(date2);
    }
    private boolean isEqualOrBefore(ZonedDateTime date1, ZonedDateTime date2) {
        if(date2 == null){
            return false;
        }
        return date1.isEqual(date2) || date1.isBefore(date2);
    }


    private Stream<UserEventLogDto> getUserEventLogStream(Subscription subscription, LocalDate endDate){
        // 구독 시작일과 종료일
        ZonedDateTime subscribedAt = subscription.getSubscribedAt();
        ZonedDateTime canceledAt = subscription.getCanceledAt();

        // endDate와 canceledAt 중 더 이른 날짜를 계산
        LocalDate actualEndDate = canceledAt.toLocalDate().isBefore(endDate) ? canceledAt.toLocalDate() : endDate;

        // 구독 시작일부터 실제 종료일까지 날짜 생성
        return subscribedAt.toLocalDate().datesUntil(actualEndDate.plusDays(1))
                .map(date -> {
                    // 해당 날짜의 로그를 찾는다
                    return subscription.getLogs().stream()
                            .filter(log -> log.getCreatedAt().toLocalDate().equals(date))
                            .findFirst()
                            .map(UserEventLogDto::of) // 로그가 있으면 해당 로그를 Dto로 변환
                            .orElseGet(() -> new UserEventLogDto(
                                    subscription.getEventEntity().getId(),
                                    subscription.getEventEntity().getEventName(),
                                    false,
                                    ZonedDateTime.of(date, LocalTime.MIN, ZoneId.systemDefault())
                            )); // 로그가 없으면 기본 Dto 생성
                });
    }
}