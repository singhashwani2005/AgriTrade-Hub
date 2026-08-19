package com.myproject.AgritradeHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.AgritradeHub.Model.ProductCategory;

public interface AddCategoryRepository  extends JpaRepository<ProductCategory, Long>{

}