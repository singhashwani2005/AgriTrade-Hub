package com.myproject.AgritradeHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.myproject.AgritradeHub.Model.Users;
import com.myproject.AgritradeHub.Model.Users.userRole;

public interface UserRepository extends JpaRepository<Users, Long>{

	boolean existsByEmail(String email);

	Users findByEmail(String email);

	List<Users> findAllByRole(userRole farmer);

}