package com.nect.friendlymony.Model.Questions;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Data{

	@SerializedName("employee")
	private List<EmployeeItem> employee;

	@SerializedName("education_qualification")
	private List<EducationQualificationItem> educationQualification;

	public void setEmployee(List<EmployeeItem> employee){
		this.employee = employee;
	}

	public List<EmployeeItem> getEmployee(){
		return employee;
	}

	public void setEducationQualification(List<EducationQualificationItem> educationQualification){
		this.educationQualification = educationQualification;
	}

	public List<EducationQualificationItem> getEducationQualification(){
		return educationQualification;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"employee = '" + employee + '\'' + 
			",education_qualification = '" + educationQualification + '\'' + 
			"}";
		}
}