package com.example.capstone.repository;

import com.example.capstone.enumaration.UserRole;
import com.example.capstone.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role,Long> {

  Optional<Role> findByRole(UserRole role);


}
