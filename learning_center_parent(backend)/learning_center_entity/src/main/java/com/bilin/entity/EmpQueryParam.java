package com.bilin.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EmpQueryParam {
    private Integer page = 1; // Page number
    private Integer pageSize = 10; // Records per page
    private String name; // Employee name
    private Integer gender; // Gender

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; // Employment start date

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end; // Employment end date
}
