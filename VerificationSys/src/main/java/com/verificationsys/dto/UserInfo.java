package com.verificationsys.dto;

import java.util.Date;

public class UserInfo {
	private String name;
	private String DOB;
	private String gender;
	private int age;
	private String nationality;
	private String verificationStatus;
	
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}

	public UserInfo(String name, String dOB, String gender, int age, String nationality, String verificationStatus) {
		super();
		this.name = name;
		DOB = dOB;
		this.gender = gender;
		this.age = age;
		this.nationality = nationality;
		this.verificationStatus = verificationStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDOB() {
		return DOB;
	}

	public void setDOB(String dOB) {
		DOB = dOB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}
	
}