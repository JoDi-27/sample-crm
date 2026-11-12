package com.example.samplecrm.company;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByNameContainingIgnoreCase(String name, Sort sort);
}
