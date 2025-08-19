package com.bilin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Integer id; // ID
    private String courseName; // Course name
    private String room; // Classroom number
    private LocalDate beginDate; // Start date
    private LocalDate endDate; // End date
    private Integer headTeacherId; // Head teacher ID
    private Integer subject; // Subject - 1 = Java, 2 = Frontend, 3 = Big Data, 4 = Python, 5 = Go, 6 = Embedded Systems
    private LocalDateTime createTime; // Creation time
    private LocalDateTime updateTime; // Last update time

    private String headTeacherName; // Head teacher name
    private String courseStatus; // Course status - Not started, In progress, Completed
}