package com.example.tvdkmedical;

import android.content.Intent;

public class Users {

    private Integer userId;
    private String name;
    private String email;
    private String password;
    private String idNumber;
    public Users(Integer userId, String name, String email, String password, String idNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.idNumber = idNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIdNumber() {
        return idNumber;
    }



}
