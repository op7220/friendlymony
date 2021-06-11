package com.nect.friendlymony.Model.GetOnlineUserStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseGetOnlineUserStatus implements Serializable {
    @SerializedName("data")
    @Expose
    private DataGetOnlineUserStatus data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public DataGetOnlineUserStatus getData() {
        return data;
    }

    public void setData(DataGetOnlineUserStatus data) {
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
}
