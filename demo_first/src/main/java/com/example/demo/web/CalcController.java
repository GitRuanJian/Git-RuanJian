package com.example.demo.web;

import com.example.demo.common.Result;
import com.example.demo.services.CalcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CalcController {
    @Resource
    private CalcService calcService;

    @GetMapping("/add")
    public Result add(int a,int b){
        return calcService.add(a, b);
    }

    @GetMapping("/minus")
    public Result minus(int a,int b){
        return calcService.minus(a, b);
    }

    @GetMapping("/multi")
    public Result multi(int a,int b){
        return calcService.multi(a, b);
    }

    @GetMapping("/divide")
    public Result divide(int a,int b){
        return calcService.divide(a, b);
    }


}