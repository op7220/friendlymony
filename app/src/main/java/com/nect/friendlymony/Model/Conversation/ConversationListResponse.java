package com.nect.friendlymony.Model.Conversation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ConversationListResponse {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;
    @SerializedName("totalUnreadCount")
    private String totalUnreadCount;
    @SerializedName("is_active")
    private String is_active;

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalUnreadCount() {
        return totalUnreadCount;
    }

    public void setTotalUnreadCount(String totalUnreadCount) {
        this.totalUnreadCount = totalUnreadCount;
    }

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	@Override
    public String toString() {
        return
                "ConversationResponse{" +
                        "data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}