package com.myproject.AgritradeHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.AgritradeHub.Model.Products;
import com.myproject.AgritradeHub.Model.Products.ProductStatus;
import com.razorpay.Product;

public interface ProductRepository  extends JpaRepository<Products, Long>{

	

	List<Products> findAllByCategoryAndStatus(String category, ProductStatus available);

}