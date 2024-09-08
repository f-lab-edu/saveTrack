package com.fthon.save_track.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommonResponse<T> {
    private final T data;
}