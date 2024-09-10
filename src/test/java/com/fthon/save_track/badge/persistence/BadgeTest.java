package com.fthon.save_track.badge.persistence;

import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.user.persistence.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BadgeTest {

    @PersistenceContext
    private EntityManager em;


    @Test
    @DisplayName("뱃지와 뱃지 도전과제 전략을 함께 영속화할 수 있다.")
    public void testSaveBadgeAndStrategy() throws Exception{
        BadgeChallengeStrategy strategy = new TotalCategoryCountStrategy(10);

        Badge badge = new Badge("뱃지 1", strategy);

        em.persist(badge);

        assertThat(em.contains(badge)).isTrue();
        assertThat(em.contains(strategy)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getTotalCategoryTestData")
    @DisplayName("사용자의 전체 EventLog의 갯수를 측정하여 뱃지를 얻을 수 있는지 체크할 수 있다.")
    public void testCheckTotalCategory(int logCount, boolean expected) throws Exception{
        //given
        BadgeChallengeStrategy strategy = new TotalCategoryCountStrategy(1);
        Badge badge = new Badge("뱃지 1", strategy);
        User user = new User("nickname", 1234L, "email@email.com");
        Event event = new Event();

        for(int i = 0; i < logCount; i++){
            user.addLog(event, true);
        }

        //when
        boolean actual = badge.getStrategy().check(user);

        //then
        assertThat(actual).isEqualTo(expected);
    
    }
    
    @Test
    @DisplayName("사용자의 전체 EventLog의 갯수를 집계하여 뱃지를 얻을수 있는지 체크할 때, false로 체크한 것은 갯수에 포함하지 않는다.")
    public void testCheckFalseTotalCategorySum() throws Exception{
        //given
        BadgeChallengeStrategy strategy = new TotalCategoryCountStrategy(1);
        Badge badge = new Badge("뱃지 1", strategy);
        User user = new User("nickname", 1234L, "email@email.com");
        Event event = new Event();

        user.addLog(event, false);

        //when
        boolean actual = badge.getStrategy().check(user);

        //then
        assertThat(actual).isFalse();

    }



    private static Stream<Arguments> getTotalCategoryTestData(){
        return Stream.of(
                Arguments.of(0, false),
                Arguments.of(1, true),
                Arguments.of(2, true)
        );
    }

}