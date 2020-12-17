package com.test.dto;

import com.test.dto.base.BaseDTO;

public class UserDTO extends BaseDTO {

    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
