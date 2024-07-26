package com.bookrentalsystem.bks.dto.book;

import com.bookrentalsystem.bks.dto.author.AuthorResponse;
import com.bookrentalsystem.bks.dto.category.CategoryResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String name;
    private Integer page;
    private String isbn;
    private Double rating;
    private Integer stock;
    private String published_date;
    private String image_path;
    private MultipartFile imageFile;
    private MultipartFile contentFile;
    private Long bookContentId;
    private Long categoryId;
    private String description;
    private List<Long> authorsId;
}
