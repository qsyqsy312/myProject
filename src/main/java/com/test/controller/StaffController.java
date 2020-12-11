package com.test.controller;


import com.test.service.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("staff")
public class StaffController {


    @Autowired
    private IStaffService staffService;


    @RequestMapping(value = "query",method = RequestMethod.GET)
    @ResponseBody
    public Object query(@RequestParam Map<String,Object> queryParam){
        return staffService.list(queryParam);
    }

}
