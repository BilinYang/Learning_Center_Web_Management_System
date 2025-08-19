package com.bilin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseQueryParam {
    private String courseName; // Course name

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; // Start time for range filtering (course end date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end; // End time for range filtering (course end date)

    private Integer page = 1; // Page number for paginated query (defaults to 1 if not specified)
    private Integer PageSize = 10; // Number of records per page in paginated query (defaults to 10 if not specified)
}
