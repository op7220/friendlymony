package com.nect.friendlymony.Model.CheckLogin;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
@JsonIgnoreProperties
 @Generated("com.robohorse.robopojogenerator")
public class LoginCheckResponse {

  /*  @JsonProperty("token")
    private String token;
*/
/*

    @JsonProperty("data")
    private Data data;
*/

   /* @JsonProperty("success")
    private boolean success;

    @JsonProperty("registered")
    private boolean registered;

    @JsonProperty("message")
    private String message;*/

    @SerializedName("success")
    private boolean success;

    @SerializedName("registered")
    private boolean registered;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @Nullable
    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}