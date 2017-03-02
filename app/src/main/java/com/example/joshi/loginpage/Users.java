package com.example.joshi.loginpage;

/**
 * Created by joshi on 13-02-2017.
 */

public class Users {
    public String name;
    public String email;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Users(String name,String email) {
        this.name=name;
        this.email=email;

    }
    public String returnNameFromUsers(){
     return name;
    }
    public String returnEmailFromUsers(){
        return email;
    }
}
