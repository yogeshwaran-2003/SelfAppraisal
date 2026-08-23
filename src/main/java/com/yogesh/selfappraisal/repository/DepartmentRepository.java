package com.yogesh.selfappraisal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yogesh.selfappraisal.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    Optional<Department> findByDepartmentId(int departmentId);
    List<Department> findByCompanyCompanyId(int companyId);

}
