package com.bilin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentQueryParam {
    private String studentName;
    private Integer degree; // Highest education level: 1 = Middle school, 2 = High school, 3 = Technical College, 4 = Bachelor's, 5 = Master's, 6 = Doctorate
    private Integer courseId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
