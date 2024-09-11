package com.fthon.save_track.auth.controller;


import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.service.AuthService;
import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 관련 API", description = "OAuth2 로그인 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OAuth2 로그인", description = "OAuth2 로그인을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 로그인된 경우로, Access Token은 Authorization 헤더에 담겨서 전달됩니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/oauth")
    public void oAuthLogin(
            @RequestBody OAuth2LoginRequest reqBody,
            HttpServletResponse resp

    ){
        String jwt = authService.doOAuth2Login(reqBody);

        resp.setHeader(HttpHeaders.AUTHORIZATION, jwt);
    }

    @Operation(summary = "개발용 로그인", description = "개발할 때 사용할 임시 API입니다.")
    @PostMapping("/temp")
    public CommonResponse<String> tempLogin(
            @RequestParam String email
    ){
        return new CommonResponse<>(authService.loginByEmail(email));
    }

}