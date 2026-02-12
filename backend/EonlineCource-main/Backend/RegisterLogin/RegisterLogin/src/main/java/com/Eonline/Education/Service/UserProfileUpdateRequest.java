package com.Eonline.Education.Service;



public class UserProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String location;
    private String dateOfBirth;
    private String website;
    private String gender;

    // Constructors
    public UserProfileUpdateRequest() {}

    public UserProfileUpdateRequest(String firstName, String lastName, String phoneNumber,
                                    String location, String dateOfBirth, String website, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.dateOfBirth = dateOfBirth;
        this.website = website;
        this.gender = gender;
    }

    // Getters and Setters
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}