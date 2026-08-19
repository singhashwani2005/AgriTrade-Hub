package com.myproject.AgritradeHub.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String productName;
	private double pricePerUnit;
	private int quantity;
	@ManyToOne
	private Users farmer;
	private double offerPrice;
	private String category;
	
	private String productImage;
	@Column(length = 1000)
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	
	public enum ProductStatus{
		AVAILABLE,OUT_OF_STOCK
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public double getPricePerUnit() {
		return pricePerUnit;
	}


	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuentity(int quentity) {
		this.quantity = quantity;
	}


	public Users getFarmer() {
		return farmer;
	}


	public void setFarmer(Users farmer) {
		this.farmer = farmer;
	}


	public double getOfferPrice() {
		return offerPrice;
	}


	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getProductImage() {
		return productImage;
	}


	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}


	public ProductStatus getStatus() {
		return status;
	}


	public void setStatus(ProductStatus status) {
		this.status = status;
	}
	
	

}
