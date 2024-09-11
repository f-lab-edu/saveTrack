package com.fthon.save_track.auth.controller;


import com.fthon.save_track.auth.dto.OAuth2LoginRequest;
import com.fthon.save_track.auth.dto.LoginResponse;
import com.fthon.save_track.auth.service.AuthService;
import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 관련 API", description = "OAuth2 로그인 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OAuth2 로그인", description = "OAuth2 로그인을 처리합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 로그인된 경우로, Access Token은 Response Body에 담겨서 전달됩니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/oauth")
    public LoginResp oAuthLogin(
            @RequestBody OAuth2LoginRequest reqBody

    ){
        String jwt = authService.doOAuth2Login(reqBody);

        return new LoginResp(200, new LoginResponse(jwt));
    }

    @Operation(summary = "개발용 로그인", description = "개발할 때 사용할 임시 API입니다.")
    @PostMapping("/temp")
    public LoginResp tempLogin(
            @RequestParam String email
    ){
        String jwt = authService.loginByEmail(email);
        return new LoginResp(200, new LoginResponse(jwt));
    }


    public static class LoginResp extends CommonResponse<LoginResponse>{

        public LoginResp(int code, String message, LoginResponse data) {
            super(code, message, data);
        }

        public LoginResp(int code, LoginResponse data) {
            super(code, data);
        }
    }

}