package com.fthon.save_track.common.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;

    public CommonResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }
}