package com.fthon.save_track.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ErrorResponse {

    private final String code;
    private final String message;

}