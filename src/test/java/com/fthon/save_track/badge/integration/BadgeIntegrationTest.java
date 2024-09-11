package com.fthon.save_track.badge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.badge.controller.BadgeController;
import com.fthon.save_track.badge.dto.AcquiredBadgeDto;
import com.fthon.save_track.badge.persistence.Badge;
import com.fthon.save_track.badge.persistence.IndividualCategoryCountStrategy;
import com.fthon.save_track.badge.persistence.TotalCategoryCountStrategy;
import com.fthon.save_track.badge.repository.BadgeRepository;
import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.event.persistence.CategoryRepository;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserBadge;
import com.fthon.save_track.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BadgeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private BadgeService badgeService;


    @Test
    @DisplayName("사용자가 얻지 못한 뱃지 리스트를 조회해 사용자가 뱃지 조건을 만족시켰는지 확인하고, 만족시킨 뱃지들을 DB에 저장하여 반환한다.")
    public void testSaveAcquiredBadges() throws Exception{
        //given
        User user = new User();
        Category category1 = new Category();
        Category category2 = new Category();

        Event event1 = new Event(category1, List.of(), "이벤트", "내용", "메시지1", "메시지2", "메시지3");
        Event event2 = new Event(category2, List.of(), "이벤트2", "내용", "메시지1", "메시지2", "메시지3");

        user.addLog(event1, true);
        user.addLog(event1, true);
        user.addLog(event2, true);

        Badge badge1 = new Badge("카테고리 1 절약왕", new IndividualCategoryCountStrategy(2, category1));
        Badge badge2 = new Badge("카테고리 2 절약왕", new IndividualCategoryCountStrategy(2, category2));
        Badge badge3 = new Badge("전체 3개이상 절약왕", new TotalCategoryCountStrategy(3));

        categoryRepository.saveAll(List.of(category1, category2));
        eventRepository.saveAll(List.of(event1, event2));
        badgeRepository.saveAll(List.of(badge1, badge2, badge3));
        userRepository.save(user);

        //when
        List<AcquiredBadgeDto> acquiredBadges = badgeService.updateUserBadges(user);

        //then
        assertThat(acquiredBadges.stream().map(AcquiredBadgeDto::getBadgeId)).containsAll(List.of(badge1.getId(), badge3.getId()));
        assertThat(user.getBadges().stream().map(UserBadge::getBadge)).containsAll(List.of(badge1, badge3));
    }

    @Test
    @DisplayName("뱃지 전체 리스트를 조회할 수 있다.")
    void testSearchBadges() throws Exception{
        // given
        User user = new User("유저", 123L, "email@email.com");
        Category category = new Category("카테고리 1");

        Event event = new Event(category, List.of(), "이벤트", "내용", "메시지1", "메시지2", "메시지3");

        Badge badge1 = new Badge("카테고리 1 절약왕", new IndividualCategoryCountStrategy(2, category));
        Badge badge2 = new Badge("전체 3개이상 절약왕", new TotalCategoryCountStrategy(3));

        user.addBadge(badge1);

        categoryRepository.save(category);
        eventRepository.save(event);
        badgeRepository.saveAll(List.of(badge1, badge2));
        userRepository.save(user);
        // when
        String jwt = jwtUtils.sign(new AuthenticatedUserDto(user.getId(), user.getEmail(), user.getNickname()), new Date());
        String uri = "/api/badges";

        MvcResult mvcResult = mockMvc.perform(get(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andDo(print())
                .andReturn();


        // then
        BadgeController.BadgeListResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BadgeController.BadgeListResponse.class);

        assertThat(result.getData()).extracting("badgeId", "badgeName", "acquired")
                .containsAll(
                        List.of(
                            Tuple.tuple(badge1.getId(), badge1.getName(), true),
                            Tuple.tuple(badge2.getId(), badge2.getName(), false)
                        ));
    }

}