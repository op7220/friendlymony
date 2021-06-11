package com.nect.friendlymony.Model.Feeds;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class ImagesItem  implements Serializable {

	@SerializedName("vPhotoID")
	private int vPhotoID;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("vPhoto")
	private String vPhoto;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setVPhotoID(int vPhotoID){
		this.vPhotoID = vPhotoID;
	}

	public int getVPhotoID(){
		return vPhotoID;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	@Override
 	public String toString(){
		return 
			"ImagesItem{" + 
			"vPhotoID = '" + vPhotoID + '\'' + 
			",createdAt = '" + createdAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",vPhoto = '" + vPhoto + '\'' + 
			",id = '" + id + '\'' + 
			",updatedAt = '" + updatedAt + '\'' + 
			"}";
		}
}