package com.nect.friendlymony.Model.GetOnlineUserStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataGetOnlineUserStatus implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_online")
    @Expose
    private Integer isOnline;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("plan_id")
    @Expose
    private String planId;
    @SerializedName("is_free_trial")
    @Expose
    private String is_free_trial;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getIs_free_trial() {
        return is_free_trial;
    }

    public void setIs_free_trial(String is_free_trial) {
        this.is_free_trial = is_free_trial;
    }
}
