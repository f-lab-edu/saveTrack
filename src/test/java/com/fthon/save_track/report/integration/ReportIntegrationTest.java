package com.fthon.save_track.report.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.event.persistence.CategoryRepository;
import com.fthon.save_track.event.persistence.Event;
import com.fthon.save_track.event.persistence.EventRepository;
import com.fthon.save_track.report.controller.ReportController;
import com.fthon.save_track.report.dto.ReportDateResponse;
import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
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

import java.time.LocalDate;
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
        Category category = new Category();

        Event event1 = new Event(category, List.of(), "이벤트", "내용", "메시지1", "메시지2", "메시지3");
        Event event2 = new Event(category, List.of(), "이벤트2", "내용", "메시지1", "메시지2", "메시지3");

        user.addLog(event1, true);
        user.addLog(event2, false);


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

        List<ReportDateResponse> expectedEndDateData = user.getLogs().stream().map(log->{
            return new ReportDateResponse(log.getEvent().getId(), log.getEvent().getEventName(), log.isChecked(), log.getCreatedAt());
        }).toList();


        assertThat(actual.getCode()).isEqualTo(200);

        // startDate ~ endDate까지의 데이터가 모두 들어가있어야 한다.
        assertThat(actual.getData().keySet()).containsAll(
                List.of(
                    startDate,
                    startDate.plusDays(1),
                    startDate.plusDays(2),
                    endDate
                )
        );


        // 마지막 날에만 데이터가 들어있어야 한다.
        for(int i = 0; i < 3; i++){
            List<ReportDateResponse> expectedEmpty = actual.getData().get(startDate.plusDays(i));
            assertThat(expectedEmpty).isEmpty();
        }
        List<ReportDateResponse> actualEndDateData = actual.getData().get(endDate);

        assertThat(actualEndDateData).containsAll(expectedEndDateData);



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



}