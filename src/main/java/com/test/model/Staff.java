package com.test.model;


import com.test.model.base.BakDeleteModel;

import javax.persistence.Entity;

@Entity
public class Staff extends BakDeleteModel {


    private String name;


    private String phone;

    private Integer age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
