package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class TestController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping
    @ResponseBody
    public String a(){
        stringRedisTemplate.opsForValue().set(String.valueOf(Math.random()),"123");
        return "success";
    }
}
