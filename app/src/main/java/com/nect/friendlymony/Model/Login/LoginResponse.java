package com.nect.friendlymony.Model.Login;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class LoginResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("registered")
	private boolean registered;

	@SerializedName("message")
	private String message;

	@SerializedName("token")
	private String token;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setRegistered(boolean registered){
		this.registered = registered;
	}

	public boolean isRegistered(){
		return registered;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"LoginResponse{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			",registered = '" + registered + '\'' + 
			",message = '" + message + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}