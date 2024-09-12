package com.fthon.save_track.event.controller;


import com.fthon.save_track.auth.dto.AuthenticatedUserDto;
import com.fthon.save_track.auth.resolvers.annotation.LoginedUser;
import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.common.response.APIResponse;
import com.fthon.save_track.common.response.ApiResponseBody;
import com.fthon.save_track.common.response.ApiResponseGenerator;
import com.fthon.save_track.common.response.MessageCode;
import com.fthon.save_track.event.application.dto.request.EventCreateRequest;
import com.fthon.save_track.event.application.dto.request.EventUpdateRequest;
import com.fthon.save_track.event.application.dto.response.getDatailEventResponse;
import com.fthon.save_track.event.application.dto.response.EventSearchResponse;
import com.fthon.save_track.event.application.service.EventService;
import com.fthon.save_track.event.persistence.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/events")
@Tag(name = "이벤트 관련 API", description = "카테고리 관련 API")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 목록 조회", description = "페이지네이션과 선택적 카테고리 필터를 기반으로 이벤트 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    //content = @Content(schema = @Schema(implementation = EventListResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("")
    public APIResponse<ApiResponseBody.SuccessBody<List<Event>>> getList(
            //@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(required = false, defaultValue = "0") int page,
            //@Parameter(description = "페이지당 항목 수") @RequestParam(required = false, defaultValue = "10") int size,
            //@Parameter(description = "필터링을 위한 카테고리 ID") @RequestParam(required = false) String categoryId
    ){
        List<Event> response = eventService.getListEvent();
        return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
    }

    @Operation(summary = "이벤트 상세 정보 조회", description = "특정 이벤트의 상세 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨")
    //content = @Content(schema = @Schema(implementation = EventDetailResponse.class)))
    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{eventId}")
    public APIResponse<ApiResponseBody.SuccessBody<Event>> getDetail(
            @Parameter(description = "조회할 이벤트의 ID") @PathVariable Long eventId
    ){
        Event response = eventService.getDetailEvent(eventId);
        return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
    }

    @Operation(summary = "새 이벤트 추가", description = "새로운 이벤트를 생성합니다")
    @ApiResponse(responseCode = "201", description = "이벤트가 성공적으로 생성됨")
    @ApiResponse(responseCode = "400", description = "잘못된 입력",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{eventId}")
    public APIResponse<ApiResponseBody.SuccessBody<Void>> createEvent(
            @Parameter(description = "생성할 이벤트의 ID") //@PathVariable String eventId,
            @RequestBody EventCreateRequest request,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        eventService.createEvent(request);
        return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
    }

    @Operation(summary = "이벤트 수정", description = "기존 이벤트를 수정합니다")
    @ApiResponse(responseCode = "200", description = "이벤트가 성공적으로 수정됨")
    @ApiResponse(responseCode = "400", description = "잘못된 입력",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{eventId}")
    public APIResponse<ApiResponseBody.SuccessBody<Void>> updateEvent(
            @Parameter(description = "수정할 이벤트의 ID") @PathVariable Long eventId,
            @RequestBody EventUpdateRequest request,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        eventService.updateEvent(request, eventId);
        return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
    }


    @Operation(summary = "이벤트 삭제", description = "기존 이벤트를 삭제합니다")
    @ApiResponse(responseCode = "200", description = "이벤트가 성공적으로 삭제됨")
    @ApiResponse(responseCode = "400", description = "잘못된 입력",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{eventId}")
    public APIResponse<ApiResponseBody.SuccessBody<Void>> deleteEvent(
            @Parameter(description = "수정할 이벤트의 ID") @PathVariable Long eventId,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        eventService.deleteEvent(eventId);
        return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
    }


    @Operation(summary = "이벤트 구독", description = "현재 사용자를 특정 이벤트에 구독시킵니다")
    @ApiResponse(responseCode = "201", description = "구독이 성공적으로 생성됨")
    @ApiResponse(responseCode = "400", description = "잘못된 입력",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{eventId}/subscribe")
    public ResponseEntity<CommonResponse> subscribe(
            @Parameter(description = "구독할 이벤트의 ID") @PathVariable Long eventId,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        eventService.subscribe(eventId, userInfo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(201, null));
    }

    @Operation(summary = "이벤트 구독 취소", description = "현재 사용자의 특정 이벤트 구독을 취소합니다")
    @ApiResponse(responseCode = "200", description = "구독이 성공적으로 취소됨")
    @ApiResponse(responseCode = "404", description = "구독 또는 이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{eventId}/subscribe")
    public ResponseEntity<CommonResponse> cancelSubscribe(
            @Parameter(description = "구독 취소할 이벤트의 ID") @PathVariable String eventId,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        return ResponseEntity.ok(new CommonResponse(200, null));
    }


    @Operation(summary = "이벤트 완료 체크", description = "특정 이벤트를 완료 상태로 표시합니다")
    @ApiResponse(responseCode = "201", description = "이벤트가 성공적으로 완료 처리됨")
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{eventId}/check")
    public ResponseEntity<CommonResponse> checkEvent(
            @Parameter(description = "완료할 이벤트의 ID") @PathVariable String eventId,
            @LoginedUser AuthenticatedUserDto userInfo
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(201, null));
    }
}