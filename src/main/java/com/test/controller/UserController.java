package com.test.controller;

import com.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "query",method = RequestMethod.GET)
    @ResponseBody
    public Object query(@RequestParam Map<String,Object> queryParam){
        return userService.list(queryParam);
    }


    @RequestMapping(value = "page",method = RequestMethod.GET)
    @ResponseBody
    public Object query(@RequestParam Map<String,Object> queryParam, @PageableDefault Pageable pageable){
        return userService.page(queryParam,pageable);
    }
}
