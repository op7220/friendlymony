package com.nect.friendlymony.Model.GetSubscriptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetSubscriptions {

    @SerializedName("data")
    @Expose
    private List<DataGetSubscriptions> data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("currency_code")
    @Expose
    private String currency_code;
    @SerializedName("currency_symbol")
    @Expose
    private String currency_symbol;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<DataGetSubscriptions> getData() {
        return data;
    }

    public void setData(List<DataGetSubscriptions> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }
}
