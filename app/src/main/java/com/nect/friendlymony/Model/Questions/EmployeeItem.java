package com.nect.friendlymony.Model.Questions;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class EmployeeItem{

	@SerializedName("are_employee")
	private String areEmployee;

	@SerializedName("is_status")
	private int isStatus;

	@SerializedName("id")
	private int id;

	public void setAreEmployee(String areEmployee){
		this.areEmployee = areEmployee;
	}

	public String getAreEmployee(){
		return areEmployee;
	}

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

	@Override
 	public String toString(){
		return 
			"EmployeeItem{" + 
			"are_employee = '" + areEmployee + '\'' + 
			",is_status = '" + isStatus + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}