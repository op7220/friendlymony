package com.nect.friendlymony.Model.Block;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Data{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("isBlock")
	private int isBlock;

	@SerializedName("sender")
	private int sender;

	@SerializedName("lastConversationDate")
	private String lastConversationDate;

	@SerializedName("recipient")
	private int recipient;

	@SerializedName("convertsations_date")
	private String convertsationsDate;

	@SerializedName("id")
	private int id;

	@SerializedName("blockedBy")
	private int blockedBy;

	@SerializedName("isdeleted")
	private int isdeleted;

	@SerializedName("updatedAt")
	private String updatedAt;

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIsBlock(int isBlock){
		this.isBlock = isBlock;
	}

	public int getIsBlock(){
		return isBlock;
	}

	public void setSender(int sender){
		this.sender = sender;
	}

	public int getSender(){
		return sender;
	}

	public void setLastConversationDate(String lastConversationDate){
		this.lastConversationDate = lastConversationDate;
	}

	public String getLastConversationDate(){
		return lastConversationDate;
	}

	public void setRecipient(int recipient){
		this.recipient = recipient;
	}

	public int getRecipient(){
		return recipient;
	}

	public void setConvertsationsDate(String convertsationsDate){
		this.convertsationsDate = convertsationsDate;
	}

	public String getConvertsationsDate(){
		return convertsationsDate;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setBlockedBy(int blockedBy){
		this.blockedBy = blockedBy;
	}

	public int getBlockedBy(){
		return blockedBy;
	}

	public void setIsdeleted(int isdeleted){
		this.isdeleted = isdeleted;
	}

	public int getIsdeleted(){
		return isdeleted;
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
			"Data{" + 
			"createdAt = '" + createdAt + '\'' + 
			",isBlock = '" + isBlock + '\'' + 
			",sender = '" + sender + '\'' + 
			",lastConversationDate = '" + lastConversationDate + '\'' + 
			",recipient = '" + recipient + '\'' + 
			",convertsations_date = '" + convertsationsDate + '\'' + 
			",id = '" + id + '\'' + 
			",blockedBy = '" + blockedBy + '\'' + 
			",isdeleted = '" + isdeleted + '\'' + 
			",updatedAt = '" + updatedAt + '\'' + 
			"}";
		}
}