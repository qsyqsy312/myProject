package com.test.controller;

import com.test.model.User;
import com.test.repository.UserRepository;
import com.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
