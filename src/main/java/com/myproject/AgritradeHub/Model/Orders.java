package com.myproject.AgritradeHub.Model;

import java.time.LocalDateTime;

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
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String productName;
	private double pricePerUnit;
	private int quantity;
	private LocalDateTime orderDate;
	private LocalDateTime deliveryDate;
	@ManyToOne
	private Users farmer;
	@ManyToOne
	private Users merchant;
	
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	
	public enum OrderStatus {
		CONFIRMED,DELIVERED, CANCELLED,REFUNDED
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


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public LocalDateTime getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}


	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public Users getFarmer() {
		return farmer;
	}


	public void setFarmer(Users farmer) {
		this.farmer = farmer;
	}


	public Users getMerchant() {
		return merchant;
	}


	public void setMerchant(Users merchant) {
		this.merchant = merchant;
	}


	public OrderStatus getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	

}
