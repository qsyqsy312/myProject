package com.test.model;


import com.test.model.base.BaseModel;

import javax.persistence.*;

@Entity
public class User extends BaseModel {

    @Column
    private String userName;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id",foreignKey =@ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Staff staff;


    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
