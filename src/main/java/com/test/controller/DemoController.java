package com.test.controller;

import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("demo")
public class DemoController {


    @Autowired
    private ModelService modelService;

    @GetMapping("listModels")
    public List<Model> list(){
        return null;
    }

}
