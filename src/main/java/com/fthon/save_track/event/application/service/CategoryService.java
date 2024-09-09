package com.fthon.save_track.event.application.service;

import com.fthon.save_track.event.persistence.Category;
import com.fthon.save_track.event.persistence.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategory() {

        List<String> categories = Arrays.asList("환경", "돈", "시간");
        for (String categoryName : categories){
            Category category = Category.builder()
                    .name(categoryName)
                    .build();
            categoryRepository.save(category);
        }
    }

    public List<Category> getCategory() {
        Stream<Category> entity = categoryRepository.getList();
        return entity
                .collect(Collectors.toList());
    }
}
