package com.bilin.controller;

import com.bilin.entity.Emp;
import com.bilin.entity.EmpLoginInfo;
import com.bilin.entity.Result;
import com.bilin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping
    public Result login(@RequestBody Emp emp) {
        log.info("Employee login: {}", emp);
        EmpLoginInfo loginInfo = empService.login(emp);
        if (loginInfo == null) {
            return Result.error("Username or password is incorrect");
        }
        return Result.success(loginInfo);
    }
}
