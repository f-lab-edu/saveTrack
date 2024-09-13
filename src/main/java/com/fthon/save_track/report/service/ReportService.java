package com.fthon.save_track.report.service;


import com.fthon.save_track.report.dto.ReportDateResponse;
import com.fthon.save_track.user.dto.UserEventLogDto;
import com.fthon.save_track.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserService userService;

    /**
     * 사용자의 이벤트 클리어 여부를 날짜별로 조회하는 메서드
     * @author minseok kim
    */
    public List<ReportDateResponse> getReportDateIn(Long userId, LocalDate startDate, LocalDate endDate) {
        List<UserEventLogDto> eventLogs = userService.getEventLogDateIn(userId, startDate, endDate);

        return eventLogs.stream().map(dto->
                new ReportDateResponse(
                    dto.getEventId(),
                    dto.getCategoryId(),
                    dto.getCheckTime().toInstant().getEpochSecond(),
                    dto.getEventName(),
                    dto.isCheck(),
                    dto.getCheckTime()
                )).toList();
    }



    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {

        return startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());
    }
}