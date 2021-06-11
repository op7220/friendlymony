package com.nect.friendlymony.Model.Feeddetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UserData {

    @SerializedName("km")
    private int km;

    @SerializedName("images")
    private List<ImagesItem> images;

    @SerializedName("last")
    private String last;

    @SerializedName("is_politics")
    private String isPolitics;

    @SerializedName("vAbout")
    private String vAbout;

    @SerializedName("first")
    private String first;

    @SerializedName("is_qualification")
    private String isQualification;

    @SerializedName("vBirthdate")
    private String vBirthdate;

    @SerializedName("is_employee")
    private String isEmployee;

    @SerializedName("is_smoke")
    private String isSmoke;

    @SerializedName("is_drink")
    private String isDrink;

    @SerializedName("is_earn")
    private String isEarn;

    @SerializedName("id")
    private int id;

    @SerializedName("lang")
    private String lang;

    @SerializedName("vShowMyAge")
    private String vShowMyAge;

    @SerializedName("vGender")
    private String vGender;

    @SerializedName("MatchPercentage")
    private int matchPercentage;

    @SerializedName("email")
    private String email;

    @SerializedName("lat")
    private String lat;

    @SerializedName("vDistanceInvisible")
    private String vDistanceInvisible;

    @SerializedName("vAge")
    private int vAge;

    @SerializedName("sender_device_token")
    @Expose
    private String sender_device_token;
    @SerializedName("sender_firebase_token")
    @Expose
    private String sender_firebase_token;
    @SerializedName("receiver_device_token")
    @Expose
    private String receiver_device_token;
    @SerializedName("receiver_firebase_token")
    @Expose
    private String receiver_firebase_token;
    @SerializedName("device_token")
    @Expose
    private String device_token;
    @SerializedName("firebase_token")
    @Expose
    private String firebase_token;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public int getKm() {
        return km;
    }

    public void setImages(List<ImagesItem> images) {
        this.images = images;
    }

    public List<ImagesItem> getImages() {
        return images;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLast() {
        return last;
    }

    public void setIsPolitics(String isPolitics) {
        this.isPolitics = isPolitics;
    }

    public String getIsPolitics() {
        return isPolitics;
    }

    public void setVAbout(String vAbout) {
        this.vAbout = vAbout;
    }

    public String getVAbout() {
        return vAbout;
    }

    public void setIsQualification(String isQualification) {
        this.isQualification = isQualification;
    }

    public String getIsQualification() {
        return isQualification;
    }

    public void setVBirthdate(String vBirthdate) {
        this.vBirthdate = vBirthdate;
    }

    public String getVBirthdate() {
        return vBirthdate;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public void setIsSmoke(String isSmoke) {
        this.isSmoke = isSmoke;
    }

    public String getIsSmoke() {
        return isSmoke;
    }

    public void setIsDrink(String isDrink) {
        this.isDrink = isDrink;
    }

    public String getIsDrink() {
        return isDrink;
    }

    public void setIsEarn(String isEarn) {
        this.isEarn = isEarn;
    }

    public String getIsEarn() {
        return isEarn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setVShowMyAge(String vShowMyAge) {
        this.vShowMyAge = vShowMyAge;
    }

    public String getVShowMyAge() {
        return vShowMyAge;
    }

    public void setVGender(String vGender) {
        this.vGender = vGender;
    }

    public String getVGender() {
        return vGender;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setVDistanceInvisible(String vDistanceInvisible) {
        this.vDistanceInvisible = vDistanceInvisible;
    }

    public String getVDistanceInvisible() {
        return vDistanceInvisible;
    }

    public void setVAge(int vAge) {
        this.vAge = vAge;
    }

    public int getVAge() {
        return vAge;
    }

    public String getSender_device_token() {
        return sender_device_token;
    }

    public void setSender_device_token(String sender_device_token) {
        this.sender_device_token = sender_device_token;
    }

    public String getSender_firebase_token() {
        return sender_firebase_token;
    }

    public void setSender_firebase_token(String sender_firebase_token) {
        this.sender_firebase_token = sender_firebase_token;
    }

    public String getReceiver_device_token() {
        return receiver_device_token;
    }

    public void setReceiver_device_token(String receiver_device_token) {
        this.receiver_device_token = receiver_device_token;
    }

    public String getReceiver_firebase_token() {
        return receiver_firebase_token;
    }

    public void setReceiver_firebase_token(String receiver_firebase_token) {
        this.receiver_firebase_token = receiver_firebase_token;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    @Override
    public String toString() {
        return
                "UserData{" +
                        "km = '" + km + '\'' +
                        ",images = '" + images + '\'' +
                        ",last = '" + last + '\'' +
                        ",is_politics = '" + isPolitics + '\'' +
                        ",vAbout = '" + vAbout + '\'' +
                        ",is_qualification = '" + isQualification + '\'' +
                        ",vBirthdate = '" + vBirthdate + '\'' +
                        ",is_employee = '" + isEmployee + '\'' +
                        ",is_smoke = '" + isSmoke + '\'' +
                        ",is_drink = '" + isDrink + '\'' +
                        ",is_earn = '" + isEarn + '\'' +
                        ",id = '" + id + '\'' +
                        ",lang = '" + lang + '\'' +
                        ",vShowMyAge = '" + vShowMyAge + '\'' +
                        ",vGender = '" + vGender + '\'' +
                        ",matchPercentage = '" + matchPercentage + '\'' +
                        ",email = '" + email + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",vDistanceInvisible = '" + vDistanceInvisible + '\'' +
                        ",vAge = '" + vAge + '\'' +
                        "}";
    }
}