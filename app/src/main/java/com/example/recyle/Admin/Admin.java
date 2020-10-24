package com.example.recyle.Admin;

public class Admin {

    public String idAdmin,password, name, email, phone;

    public Admin() {

    }

    public Admin(String idAdmin, String password, String name, String email,  String phone) {
        this.idAdmin = idAdmin;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
