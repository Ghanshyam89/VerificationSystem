package com.verificationsys.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	String name;
	int age;
	String gender;
	Date dob;
	String nationality;
	
	@Column(name="verification_status")
	String verificationStatus;
	
	@Column(name="date_created")
	Date dateCreated;
	
	@Column(name="date_modified")
	Date dateModified;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User( String name, int age, String gender, Date dob, String nationality,
			String verificationStatus, Date dateCreated, Date dateModified) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.dob = dob;
		this.nationality = nationality;
		this.verificationStatus = verificationStatus;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", age=" + age + ", gender=" + gender + ", dob=" + dob
				+ ", nationality=" + nationality + ", verificationStatus=" + verificationStatus + ", dateCreated="
				+ dateCreated + ", dateModified=" + dateModified + "]";
	}

	
}
