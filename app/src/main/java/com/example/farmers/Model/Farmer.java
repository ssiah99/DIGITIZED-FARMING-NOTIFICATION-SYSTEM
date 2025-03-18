package com.example.farmers.Model;

public class Farmer {
    public String name, phone, age, farmSize, county, constituency, ward,idnumber,emails;

    public Farmer() {}

    public Farmer(String name, String phone, String age, String farmSize, String county, String constituency, String ward, String idnumber, String emails) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.farmSize = farmSize;
        this.county = county;
        this.constituency = constituency;
        this.ward = ward;
        this.idnumber = idnumber;
        this.emails = emails;
    }

    public Farmer(String name, String ward, String idnumber, String emails) {
        this.name = name;
        this.ward = ward;
        this.idnumber = idnumber;
        this.emails = emails;
    }
}

