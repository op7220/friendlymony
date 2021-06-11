package com.nect.friendlymony.Model.Images;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DataItem{

	@SerializedName("vPhotoID")
	private int vPhotoID;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("vPhoto")
	private String vPhoto;

	@SerializedName("id")
	private int id;

	public void setVPhotoID(int vPhotoID){
		this.vPhotoID = vPhotoID;
	}

	public int getVPhotoID(){
		return vPhotoID;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setVPhoto(String vPhoto){
		this.vPhoto = vPhoto;
	}

	public String getVPhoto(){
		return vPhoto;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"vPhotoID = '" + vPhotoID + '\'' + 
			",user_id = '" + userId + '\'' + 
			",vPhoto = '" + vPhoto + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}