package com.test.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder{

    @Autowired
    private static  ApplicationContext applicationContext ;


    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }


    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


    public static Object getBean(Class clazz) {
        return applicationContext.getBean(clazz);
    }
}
