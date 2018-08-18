package com.burn.burn.controller.login;

/**
 * Created by MihailButnaru on 18/08/2018.
 */

public class LoginProfiles {
    private String id;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String line1;
    private String line2;

    public LoginProfiles(String firstName, String lastName, String city, String country, String line1, String line2) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.line1 = line1;
        this.line2 = line2;
    }

    public LoginProfiles(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public LoginProfiles(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public LoginProfiles() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }
}
