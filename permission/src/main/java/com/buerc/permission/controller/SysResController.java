package com.buerc.permission.controller;

import com.buerc.common.web.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/res")
public class SysResController {
    @PostMapping("/add")
    public Result add(){
        return Result.success();
    }
}
