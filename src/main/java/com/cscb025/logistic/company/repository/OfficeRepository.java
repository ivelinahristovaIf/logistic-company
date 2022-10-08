package com.cscb025.logistic.company.repository;

import com.cscb025.logistic.company.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, String> {

    Optional<Office> findByName(String name);
}
