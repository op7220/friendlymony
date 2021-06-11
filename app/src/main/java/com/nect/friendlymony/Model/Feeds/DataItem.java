package com.nect.friendlymony.Model.Feeds;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class DataItem implements Serializable {

    @SerializedName("km")
    private int km;

    @SerializedName("images")
    private List<ImagesItem> images;

    @SerializedName("last")
    private String last;

    @SerializedName("is_politics")
    private String isPolitics;

    @SerializedName("IsLiked")
    private String isLiked;

    @SerializedName("vAbout")
    private String vAbout;

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
    private String id;

    @SerializedName("lang")
    private String lang;

    @SerializedName("vShowMyAge")
    private String vShowMyAge;

    @SerializedName("vGender")
    private String vGender;

    @SerializedName("MatchPercentage")
    private int matchPercentage;

    @SerializedName("first")
    private String first;

    @SerializedName("email")
    private String email;

    @SerializedName("lat")
    private String lat;

    @SerializedName("vDistanceInvisible")
    private String vDistanceInvisible;

    @SerializedName("BoostStatus")
    private String BoostStatus;

    @SerializedName("vAge")
    private int vAge;

    @SerializedName("is_paid")
    private String is_paid;

    @SerializedName("is_auto_subscribe")
    private String is_auto_subscribe;

    public String getBoostStatus() {
        return BoostStatus;
    }

    public void setBoostStatus(String boostStatus) {
        BoostStatus = boostStatus;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public List<ImagesItem> getImages() {
        return images;
    }

    public void setImages(List<ImagesItem> images) {
        this.images = images;
    }

    public String getvAbout() {
        return vAbout;
    }

    public void setvAbout(String vAbout) {
        this.vAbout = vAbout;
    }

    public String getvBirthdate() {
        return vBirthdate;
    }

    public void setvBirthdate(String vBirthdate) {
        this.vBirthdate = vBirthdate;
    }

    public String getvShowMyAge() {
        return vShowMyAge;
    }

    public void setvShowMyAge(String vShowMyAge) {
        this.vShowMyAge = vShowMyAge;
    }

    public String getvGender() {
        return vGender;
    }

    public void setvGender(String vGender) {
        this.vGender = vGender;
    }

    public String getvDistanceInvisible() {
        return vDistanceInvisible;
    }

    public void setvDistanceInvisible(String vDistanceInvisible) {
        this.vDistanceInvisible = vDistanceInvisible;
    }

    public int getvAge() {
        return vAge;
    }

    public void setvAge(int vAge) {
        this.vAge = vAge;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getIsPolitics() {
        return isPolitics;
    }

    public void setIsPolitics(String isPolitics) {
        this.isPolitics = isPolitics;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getVAbout() {
        return vAbout;
    }

    public void setVAbout(String vAbout) {
        this.vAbout = vAbout;
    }

    public String getIsQualification() {
        return isQualification;
    }

    public void setIsQualification(String isQualification) {
        this.isQualification = isQualification;
    }

    public String getVBirthdate() {
        return vBirthdate;
    }

    public void setVBirthdate(String vBirthdate) {
        this.vBirthdate = vBirthdate;
    }

    public String getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getIsSmoke() {
        return isSmoke;
    }

    public void setIsSmoke(String isSmoke) {
        this.isSmoke = isSmoke;
    }

    public String getIsDrink() {
        return isDrink;
    }

    public void setIsDrink(String isDrink) {
        this.isDrink = isDrink;
    }

    public String getIsEarn() {
        return isEarn;
    }

    public void setIsEarn(String isEarn) {
        this.isEarn = isEarn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getVShowMyAge() {
        return vShowMyAge;
    }

    public void setVShowMyAge(String vShowMyAge) {
        this.vShowMyAge = vShowMyAge;
    }

    public String getVGender() {
        return vGender;
    }

    public void setVGender(String vGender) {
        this.vGender = vGender;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getVDistanceInvisible() {
        return vDistanceInvisible;
    }

    public void setVDistanceInvisible(String vDistanceInvisible) {
        this.vDistanceInvisible = vDistanceInvisible;
    }

    public int getVAge() {
        return vAge;
    }

    public void setVAge(int vAge) {
        this.vAge = vAge;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getIs_auto_subscribe() {
        return is_auto_subscribe;
    }

    public void setIs_auto_subscribe(String is_auto_subscribe) {
        this.is_auto_subscribe = is_auto_subscribe;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "km = '" + km + '\'' +
                        ",images = '" + images + '\'' +
                        ",last = '" + last + '\'' +
                        ",is_politics = '" + isPolitics + '\'' +
                        ",isLiked = '" + isLiked + '\'' +
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
                        ",first = '" + first + '\'' +
                        ",email = '" + email + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",vDistanceInvisible = '" + vDistanceInvisible + '\'' +
                        ",vAge = '" + vAge + '\'' +
                        "}";
    }
}