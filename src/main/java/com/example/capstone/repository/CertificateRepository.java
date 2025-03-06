package com.example.capstone.repository;

import com.example.capstone.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CertificateRepository extends JpaRepository<Certificate,Long> {

List<Certificate> findAllByUserId(Long userId);
}
