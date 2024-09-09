package com.fthon.save_track.auth.controller;


import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.utils.JwtUtils;
import com.fthon.save_track.common.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 관련 API", description = "OAuth2 로그인 관련 API")
public class AuthController {


    @Operation(summary = "OAuth2 로그인", description = "OAuth2 로그인을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 로그인된 경우로, Access Token은 Authorization 헤더에 담겨서 전달됩니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/oauth")
    public void oAuthLogin(
            @RequestBody OAuth2LoginRequest reqBody

    ){
        System.out.println();
    }

}