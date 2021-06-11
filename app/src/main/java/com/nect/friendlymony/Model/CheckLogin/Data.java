package com.nect.friendlymony.Model.CheckLogin;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties
@Generated("com.robohorse.robopojogenerator")
public class Data {

    @SerializedName("phone_no")
    private String phoneNo;

    @SerializedName("user_status")
    private int userStatus;

    @SerializedName("google_id")
    private String googleId;

    @SerializedName("mobile_no")
    private String mobileNo;

    @SerializedName("profile_pic")
    private String profilePic;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("facebook_id")
    private String facebookId;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("password")
    private String password;

    @SerializedName("user_type")
    private int userType;

    @SerializedName("name")
    private String name;

    @SerializedName("is_notification")
    private int isNotification;

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("user_unique_id")
    private String user_unique_id;

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsNotification(int isNotification) {
        this.isNotification = isNotification;
    }

    public int getIsNotification() {
        return isNotification;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "phone_no = '" + phoneNo + '\'' +
                        ",user_status = '" + userStatus + '\'' +
                        ",google_id = '" + googleId + '\'' +
                        ",mobile_no = '" + mobileNo + '\'' +
                        ",profile_pic = '" + profilePic + '\'' +
                        ",last_name = '" + lastName + '\'' +
                        ",facebook_id = '" + facebookId + '\'' +
                        ",country_code = '" + countryCode + '\'' +
                        ",createdAt = '" + createdAt + '\'' +
                        ",password = '" + password + '\'' +
                        ",user_type = '" + userType + '\'' +
                        ",name = '" + name + '\'' +
                        ",is_notification = '" + isNotification + '\'' +
                        ",id = '" + id + '\'' +
                        ",email = '" + email + '\'' +
                        ",updatedAt = '" + updatedAt + '\'' +
                        "}";
    }

 /*   "id": 88,
            "name": "Chirag",
            "email": "chirag.nectarbits1992+16@gmail.com",
            "last_name": "Mobile",
            "user_unique_id": "XX6CM77R",
            "country_code": "91",
            "mobile_no": "7096342439",
            "phone_no": "917096342439",
            "password": "$2a$10$e48EJdqE/ad45BCTp2cGkOnyYBnSpsGgGaF5M8AISMA2EZqNg49Uu",
            "profile_pic": "",
            "user_type": 0,
            "google_id": "",
            "facebook_id": "",
            "user_status": 1,
            "is_notification": 1,
            "is_paid": 0,
            "is_block": 0,
            "is_sign_up": 1,
            "free_crush_count": 1,
            "paid_crush_count": 0,
            "expire_date": "2019-09-06",
            "createdAt": "2019-08-31T09:41:28.000Z",
            "updatedAt": "2019-08-31T09:41:28.000Z"*/
}