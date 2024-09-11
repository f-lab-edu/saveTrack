package com.fthon.save_track.common.dto;


import lombok.*;

import java.util.Arrays;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse extends CommonResponse<ErrorResponse.ErrorInfo> {

    public ErrorResponse(int code, String message, ErrorInfo data) {
        super(code, message, data);
    }

    public ErrorResponse(int code, ErrorInfo data) {
        super(code, data);
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class ErrorInfo {
        private String[] stackTrace;

        public static ErrorInfo of(StackTraceElement[] stackTraceElements){

            return new ErrorInfo(Arrays.stream(stackTraceElements).map(StackTraceElement::toString).toArray(String[]::new));
        }
    }
}