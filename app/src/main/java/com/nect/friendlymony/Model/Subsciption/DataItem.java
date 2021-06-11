package com.nect.friendlymony.Model.Subsciption;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class DataItem {

    @SerializedName("subscription_end_date")
    private String endDate;

    @SerializedName("amount")
    private double amount;

    @SerializedName("days")
    private int day;

    @SerializedName("subscription_start_date")
    private String startDate;

    @SerializedName("converted_price")
    private String converted_price;
    @SerializedName("months")
    private String months;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getConverted_price() {
        return converted_price;
    }

    public void setConverted_price(String converted_price) {
        this.converted_price = converted_price;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "end_date = '" + endDate + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",day = '" + day + '\'' +
                        ",start_date = '" + startDate + '\'' +
                        "}";
    }
}