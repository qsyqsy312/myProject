package com.test.controller;


import com.test.dto.StaffDTO;
import com.test.service.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("staff")
public class StaffController {


    @Autowired
    private IStaffService staffService;


    @RequestMapping(value = "query",method = RequestMethod.GET)
    @ResponseBody
    public Object query(@RequestParam Map<String,Object> queryParam){
        return staffService.list(queryParam, Sort.unsorted());
    }



    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public Object save(@RequestBody StaffDTO staffDTO) throws Exception{
        return staffService.save(staffDTO);
    }

}
