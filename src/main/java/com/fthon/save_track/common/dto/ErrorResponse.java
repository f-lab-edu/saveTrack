package com.fthon.save_track.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Data
public class ErrorResponse extends CommonResponse<ErrorResponse.ErrorInfo> {

    public ErrorResponse(int code, String message, ErrorInfo data) {
        super(code, message, data);
    }

    public ErrorResponse(int code, ErrorInfo data) {
        super(code, data);
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorInfo {
        private final String[] stackTrace;
    }
}