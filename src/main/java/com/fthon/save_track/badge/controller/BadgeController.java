package com.fthon.save_track.badge.controller;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.resolvers.annotation.LoginedUser;
import com.fthon.save_track.badge.dto.BadgeSearchResponse;
import com.fthon.save_track.badge.integration.BadgeService;
import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
@Tag(name = "뱃지 관련 API", description = "뱃지 관련 API")
public class BadgeController {

    private final BadgeService badgeService;

    @Operation(summary = "뱃지 목록 조회", description = "모든 뱃지의 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
            content = @Content(schema = @Schema(implementation = BadgeListResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("")
    public ResponseEntity<BadgeListResponse> getList(
            @LoginedUser AuthenticatedUserDto userInfo
    ) {
        BadgeListResponse respBody = new BadgeListResponse(badgeService.getBadges(userInfo.getId()));
        return ResponseEntity.ok(respBody);
    }


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class BadgeListResponse extends CommonResponse<List<BadgeSearchResponse>> {
        public BadgeListResponse(List<BadgeSearchResponse> data) {
            super(data);
        }
    }
}