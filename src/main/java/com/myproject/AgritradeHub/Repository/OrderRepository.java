package com.myproject.AgritradeHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.AgritradeHub.Model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
