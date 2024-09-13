package com.fthon.save_track.report.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.event.persistence.*;
import com.fthon.save_track.report.controller.ReportController;
import com.fthon.save_track.report.dto.ReportDateResponse;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.persistence.UserEventLog;
import com.fthon.save_track.user.repository.UserRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReportIntegrationTest {

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

    @Test
    @DisplayName("사용자의 기간별 이벤트 성공 여부를 조회할 수 있다.")
    public void testGetReport() throws Exception{
        //given
        User user = new User();
        Category category = new Category("category-001", "카테고리");

        Event event1 = new Event(category, List.of(), false,  "이벤트", "내용", "메시지1", "메시지2", "메시지3", List.of());
        Event event2 = new Event(category, List.of(), false, "이벤트2", "내용", "메시지1", "메시지2", "메시지3", List.of());

        Subscription s1 = user.addSubscription(event1);
        Subscription s2 = user.addSubscription(event2);

        UserEventLog l1 = s1.addLog(true);
        UserEventLog l2 = s2.addLog(false);

        categoryRepository.save(category);
        eventRepository.saveAll(List.of(event1, event2));
        userRepository.save(user);

        //when
        String uri = "/api/reports";
        String jwt = jwtUtils.sign(AuthenticatedUserDto.of(user), new Date());

        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        MvcResult mvcResult = mockMvc.perform(get(uri)
                .param("startDate", startDate.format(formatter))
                .param("endDate", endDate.format(formatter))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        ).andDo(
                print()
        ).andReturn();

        //then
        ReportController.ReportListResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReportController.ReportListResponse.class);

        List<ReportDateResponse> expectedEndDateData = List.of(
                new ReportDateResponse(event1.getId(), category.getUid(), l1.getCreatedAt().toInstant().toEpochMilli(), event1.getEventName(), l1.isChecked(), l1.getCreatedAt()),
                new ReportDateResponse(event2.getId(), category.getUid(), l2.getCreatedAt().toInstant().toEpochMilli(), event2.getEventName(), l2.isChecked(), l2.getCreatedAt())
        );


        assertThat(actual.getCode()).isEqualTo(200);

    }

    @Test
    @DisplayName("StartDate가 EndDate보다 후면 400 오류를 반환한다.")
    public void testStartDateIsAfterEndDate() throws Exception{
        //given
        User user = new User();

        //when
        String uri = "/api/reports";
        String jwt = jwtUtils.sign(AuthenticatedUserDto.of(user), new Date());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        MvcResult mvcResult = mockMvc.perform(get(uri)
                .param("startDate", startDate.format(formatter))
                .param("endDate", endDate.format(formatter))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        ).andDo(
                print()
        ).andReturn();

        //then
        ErrorResponse errorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getCode()).isEqualTo(400);

    }

    @Test
    @DisplayName("사용자가 체크를 하지 않아도 구독을 했다면 리포트에 조회할 수 있다.")
    void testUnCheckedSubscription() throws Exception{
        // given
        User user = new User();
        Category category = new Category();

        Event event = new Event(category, List.of(), false,  "이벤트", "내용", "메시지1", "메시지2", "메시지3", List.of());
        user.addSubscription(event);

        categoryRepository.save(category);
        eventRepository.save(event);
        userRepository.save(user);

        // when
        String uri = "/api/reports";
        String jwt = jwtUtils.sign(AuthenticatedUserDto.of(user), new Date());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();

        MvcResult mvcResult = mockMvc.perform(get(uri)
                .param("startDate", date.format(formatter))
                .param("endDate", date.format(formatter))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        ).andDo(
                print()
        ).andReturn();

        // then
        ReportController.ReportListResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReportController.ReportListResponse.class);
        assertThat(actual.getCode()).isEqualTo(200);

    }

    @Test
    @DisplayName("이미 구독취소를 한 이벤트일 경우 구독 취소를 한 날까지의 데이터만 보여진다.")
    void testCancelSubscriptionDateRange() throws Exception{
        // given
        User user = new User();
        Category category = new Category();

        Event event = new Event(category, List.of(), false,  "이벤트", "내용", "메시지1", "메시지2", "메시지3", List.of());
        Subscription s = user.addSubscription(event);
        s.setSubscribedAt(ZonedDateTime.of(
                LocalDateTime.of(2024, 1, 1, 0,0,0),
                ZoneOffset.UTC
        ));

        s.setCanceledAt(
                ZonedDateTime.of(
                        LocalDateTime.of(2024, 1, 2, 0,0,0),
                        ZoneOffset.UTC
                )
        );

        categoryRepository.save(category);
        eventRepository.save(event);
        userRepository.save(user);

        // when
        String uri = "/api/reports";
        String jwt = jwtUtils.sign(AuthenticatedUserDto.of(user), new Date());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        MvcResult mvcResult = mockMvc.perform(get(uri)
                .param("startDate", LocalDate.of(2023, 12, 31).format(formatter))
                .param("endDate", LocalDate.of(2024,1,3).format(formatter))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        ).andDo(
                print()
        ).andReturn();

        // then
        ReportController.ReportListResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReportController.ReportListResponse.class);

        List<LocalDate> expectedEmptys = List.of(
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 1, 3)
        );
        List<LocalDate> expectedNotEmptys = List.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2)
        );

    }


}