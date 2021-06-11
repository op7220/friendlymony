package com.nect.friendlymony.Model.Questions;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class EducationQualificationItem{

	@SerializedName("is_status")
	private int isStatus;

	@SerializedName("id")
	private int id;

	@SerializedName("education_qualification")
	private String educationQualification;

	public void setIsStatus(int isStatus){
		this.isStatus = isStatus;
	}

	public int getIsStatus(){
		return isStatus;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setEducationQualification(String educationQualification){
		this.educationQualification = educationQualification;
	}

	public String getEducationQualification(){
		return educationQualification;
	}

	@Override
 	public String toString(){
		return 
			"EducationQualificationItem{" + 
			"is_status = '" + isStatus + '\'' + 
			",id = '" + id + '\'' + 
			",education_qualification = '" + educationQualification + '\'' + 
			"}";
		}
}