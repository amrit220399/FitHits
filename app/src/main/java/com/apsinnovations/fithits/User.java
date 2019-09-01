package com.apsinnovations.fithits;



public class User {

    public String name;
    public String email;
    public String password;

    public User() {
        name = null;
        email = null;
        password = null;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }




    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
