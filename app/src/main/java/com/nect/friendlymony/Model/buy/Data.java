package com.nect.friendlymony.Model.buy;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Data{

	@SerializedName("total_crush")
	private int totalCrush;

	@SerializedName("left_crush")
	private int leftCrush;

	@SerializedName("spent_crush")
	private int spentCrush;

	@SerializedName("total_boost")
	private int total_boost;

	@SerializedName("spent_boost")
	private int spent_boost;

	@SerializedName("left_boost")
	private int left_boost;

	public void setTotalCrush(int totalCrush){
		this.totalCrush = totalCrush;
	}

	public int getTotalCrush(){
		return totalCrush;
	}

	public void setLeftCrush(int leftCrush){
		this.leftCrush = leftCrush;
	}

	public int getLeftCrush(){
		return leftCrush;
	}

	public void setSpentCrush(int spentCrush){
		this.spentCrush = spentCrush;
	}

	public int getSpentCrush(){
		return spentCrush;
	}

	public int getTotal_boost() {
		return total_boost;
	}

	public void setTotal_boost(int total_boost) {
		this.total_boost = total_boost;
	}

	public int getSpent_boost() {
		return spent_boost;
	}

	public void setSpent_boost(int spent_boost) {
		this.spent_boost = spent_boost;
	}

	public int getLeft_boost() {
		return left_boost;
	}

	public void setLeft_boost(int left_boost) {
		this.left_boost = left_boost;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"total_crush = '" + totalCrush + '\'' + 
			",left_crush = '" + leftCrush + '\'' + 
			",spent_crush = '" + spentCrush + '\'' + 
			"}";
		}
}