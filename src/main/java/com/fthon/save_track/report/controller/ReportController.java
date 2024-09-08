package com.fthon.save_track.report.controller;


import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.report.dto.ReportDateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "리포트 관련", description = "리포트 조회를 위한 API")
public class ReportController {

    @Operation(summary = "기간별 리포트 조회", description = "지정된 기간의 리포트를 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
            content = @Content(schema = @Schema(implementation = ReportListResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("")
    public ResponseEntity<ReportListResponse> getReport(
            @Parameter(description = "조회 시작 날짜 (yyyy-MM-dd 형식)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "조회 종료 날짜 (yyyy-MM-dd 형식)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(null);
    }

    public static class ReportListResponse extends CommonResponse<List<ReportDateResponse>> {
        public ReportListResponse(List<ReportDateResponse> data) {
            super(data);
        }
    }
}