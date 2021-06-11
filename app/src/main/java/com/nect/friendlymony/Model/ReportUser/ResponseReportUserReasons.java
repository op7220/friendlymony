package com.nect.friendlymony.Model.ReportUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReportUserReasons {
    @SerializedName("data")
    @Expose
    private List<DataReportUserReasons> data = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<DataReportUserReasons> getData() {
        return data;
    }

    public void setData(List<DataReportUserReasons> data) {
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
