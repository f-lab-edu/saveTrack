package com.fthon.save_track.event.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategorySearchResponse {

    private String categoryId;
    private String categoryName;

}