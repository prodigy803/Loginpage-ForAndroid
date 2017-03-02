package com.example.joshi.loginpage;

/**
 * Created by joshi on 14-02-2017.
 */

public class UserCriticalData {
    public String email;


    public UserCriticalData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public  UserCriticalData(String email){
        this.email=email;

    }
}
