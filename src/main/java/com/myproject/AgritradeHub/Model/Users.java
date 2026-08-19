package com.myproject.AgritradeHub.Model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPanNumber() {
		return panCard;
	}

	public void setPanNumber(String panCard) {
		this.panCard = panCard;
	}

	public String getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(String availableTime) {
		this.availableTime = availableTime;
	}

	public userStatus getStatus() {
		return status;
	}

	public void setStatus(userStatus status) {
		this.status = status;
	}

	public userRole getRole() {
		return role;
	}

	public void setRole(userRole role) {
		this.role = role;
	}
    
	private String name;
	private String age;
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	private String contactNumber;
	@Column(unique = true,nullable = false)
	private String email;
	
	@Column(nullable = false, length =12)
	private String password;
	private String aadharNumber;
	private String merchantType;
	private String gstNo;
	@Column(length =200)
	private String profilePic;
	
	@Column(length = 500) 
	private String address;
	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	private String panCard;
	private String availableTime;
	private LocalDateTime regDate;
	
	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	@Enumerated(EnumType.STRING)
	private userStatus status;
	
	@Enumerated(EnumType.STRING)
	private userRole role;

	public enum userStatus {
		PENDING, APPROVED, DISABLED,
	}

	public enum userRole {
		ADMIN, FARMER, MERCHANT,
	}
	
}
