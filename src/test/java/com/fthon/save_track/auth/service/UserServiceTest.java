package com.fthon.save_track.auth.service;

import com.fthon.save_track.auth.dto.KakaoUserInfo;
import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.client.KakaoOAuth2Client;
import com.fthon.save_track.common.domain.BaseEntity;
import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.event.persistence.Subscription;
import com.fthon.save_track.user.dto.UserEventLogDto;
import com.fthon.save_track.user.dto.UserSubscriptionInfoDto;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import com.fthon.save_track.user.repository.UserRepository;
import com.fthon.save_track.user.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @SpyBean
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private KakaoOAuth2Client kakaoOAuth2Client;


    @Test
    @DisplayName("DB에 저장되지 않은 사용자의 카카오 토큰을 전달받아 DB에 사용자 정보를 저장하고 JWT를 반환한다.")
    public void testKakaoLogin() throws Exception{
        //given
        KakaoUserInfo kakaoUserInfo = createDummyKakaoUserInfo();
        given(kakaoOAuth2Client.getUserInfo(any())).willReturn(kakaoUserInfo);
        String deviceToken = "asdsfs";

        //when
        OAuth2LoginRequest reqBody = new OAuth2LoginRequest(
                "kakao",
                "token",
                deviceToken
        );

        String result = userService.doOAuth2Login(reqBody);

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
        String deviceToken = "asdsfs";
        User user = new User(
                kakaoUserInfo.getKakaoAccount().getName(),
                kakaoUserInfo.getId(),
                kakaoUserInfo.getKakaoAccount().getEmail(),
                deviceToken
        );
        userRepository.save(user);

        //when

        OAuth2LoginRequest reqBody = new OAuth2LoginRequest(
                "kakao",
                "token",
                deviceToken
        );

        String result = userService.doOAuth2Login(reqBody);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유저의 이벤트 로그를 특정 날짜 범위로 조회할 수 있다.")
    public void testGetUserLogListDateIn() throws Exception{
        //given
        User user = new User();
        Category category = new Category("c1", "c");
        Event event = new Event(category, List.of(), false, "name", "content", "", "", "", List.of());

        Subscription subscription = user.addSubscription(event);

        for(int i = 0; i < 3; i++){
            subscription.addLog(true);
        }

        ZonedDateTime dateTime = ZonedDateTime.of(2024, 9, 11, 0, 0, 0, 0, ZoneOffset.UTC);
        List<ZonedDateTime> dateTimes = List.of(
                dateTime,
                dateTime.plusDays(1),
                dateTime.plusDays(2)
        );


        Field subscribedAt = Subscription.class.getDeclaredField("subscribedAt");
        subscribedAt.setAccessible(true);
        subscribedAt.set(subscription, dateTime);
        for(int i = 0; i < 3; i++){
            UserEventLog log = subscription.getLogs().get(i);
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(log, dateTimes.get(i));
        }

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        List<UserEventLogDto> actual = userService.getEventLogDateIn(1L, dateTime.plusDays(1).toLocalDate(), dateTime.plusDays(2).toLocalDate());

        //then
        List<UserEventLogDto> expected = List.of(
                UserEventLogDto.of(subscription.getLogs().get(1)),
                UserEventLogDto.of(subscription.getLogs().get(2))
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("유저가 현재 구독하고 있는 이벤트의 정보를 반환할 수 있다.")
    void testGetUserCurrentSubscriptions() throws Exception{
        // given
        String deviceToken = "deviceToken";
        User user = new User(
                "nickname",
                1234L,
                "email@email.com",
                deviceToken
        );
        Event event1 = new Event();
        Event event2 = new Event(
                null,
                List.of(),
                false,
                "name",
                "content",
                "morning",
                "afternoon",
                "evening",
                List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)
        );

        Subscription s1 = user.addSubscription(event1);
        Subscription s2 = user.addSubscription(event2);

        ZonedDateTime subscribeTime = ZonedDateTime.of(
                LocalDateTime.of(2024, 9, 13, 0, 0, 0),
                ZoneId.systemDefault()
        );

        ZonedDateTime s1CancelTime = ZonedDateTime.of(
                LocalDateTime.of(2024, 9, 14, 0, 0, 0),
                ZoneId.systemDefault()
        );


        s1.setSubscribedAt(subscribeTime);
        s1.setCanceledAt(s1CancelTime);
        s2.setSubscribedAt(subscribeTime);

        eventRepository.saveAll(List.of(event1, event2));
        userRepository.save(user);
        // when
        List<UserSubscriptionInfoDto> actual = userService.getCurrentSubscribes(s1CancelTime.plusDays(1));
        // then
        assertThat(actual.get(0).getUserDeviceToken()).isEqualTo(deviceToken);
        assertThat(actual.get(0).getSubscribeEvents()).hasSize(1);
        assertThat(actual.get(0).getSubscribeEvents()).extracting(
                "eventName",
                "morningMessage",
                "afternoonMessage",
                "eveningMessage"
        ).containsExactly(
                Tuple.tuple(
                        "name",
                        "morning",
                        "afternoon",
                        "evening"
                )
        );
        assertThat(actual.get(0).getSubscribeEvents().get(0).getNotificationDayOfWeeks())
                .containsExactlyInAnyOrder(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);
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