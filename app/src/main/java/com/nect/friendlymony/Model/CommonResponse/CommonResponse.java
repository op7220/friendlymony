package com.nect.friendlymony.Model.CommonResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("is_active")
    @Expose
    private String is_active;
    @SerializedName("senderProfile")
    @Expose
    private String senderProfile;
    @SerializedName("image_sender")
    @Expose
    private String image_sender;
    @SerializedName("image_receiver")
    @Expose
    private String image_receiver;
    @SerializedName("receiverImage")
    @Expose
    private String receiverImage;

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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(String senderProfile) {
        this.senderProfile = senderProfile;
    }

    public String getImage_sender() {
        return image_sender;
    }

    public void setImage_sender(String image_sender) {
        this.image_sender = image_sender;
    }

    public String getImage_receiver() {
        return image_receiver;
    }

    public void setImage_receiver(String image_receiver) {
        this.image_receiver = image_receiver;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }
}
