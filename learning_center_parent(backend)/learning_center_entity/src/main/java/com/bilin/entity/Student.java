package com.bilin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String studentName;
    private String studentNumber;
    private Integer gender; // Gender: 1 = Male, 2 = Female
    private String phone;
    private String idCardNum; // ID card number
    private Integer isCollegeStu; // Is a college student: 1 = True, 0 = False
    private String address; // Contact address
    private Integer degree; // Highest education level: 1 = Middle school, 2 = High school, 3 = Technical College, 4 = Bachelor's, 5 = Master's, 6 = Doctorate
    private LocalDate graduationDate; // Graduation date
    private Integer courseId;
    private Short violationCount = 0; // Number of violations
    private Short penaltyPoints = 0; // Violation penalty points
    private LocalDateTime createTime; // Creation time
    private LocalDateTime updateTime; // Last update time

    private String courseName; // Course name
}
