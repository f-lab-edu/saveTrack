package com.fthon.save_track.event.application.dto.response;


import com.fthon.save_track.event.persistence.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CategorySearchResponse {

    private String categoryId;
    private String categoryName;


    public static CategorySearchResponse of(Category category){
        return new CategorySearchResponse(
                category.getUid(),
                category.getName()
        );
    }
}