package com.yogesh.selfappraisal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yogesh.selfappraisal.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
        Company findFirstByOrderByCompanyIdAsc();
}
