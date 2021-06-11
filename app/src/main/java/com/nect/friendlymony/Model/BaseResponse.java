package com.nect.friendlymony.Model;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@JsonIgnoreProperties
@Generated("com.robohorse.robopojogenerator")
public class BaseResponse {

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


    @SerializedName("message")
    private String message;



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


}