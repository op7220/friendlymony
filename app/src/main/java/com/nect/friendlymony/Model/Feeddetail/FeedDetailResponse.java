package com.nect.friendlymony.Model.Feeddetail;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class FeedDetailResponse{

	@SerializedName("userData")
	private UserData userData;

	@SerializedName("success")
	private boolean success;

	public void setUserData(UserData userData){
		this.userData = userData;
	}

	public UserData getUserData(){
		return userData;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"FeedDetailResponse{" + 
			"userData = '" + userData + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}