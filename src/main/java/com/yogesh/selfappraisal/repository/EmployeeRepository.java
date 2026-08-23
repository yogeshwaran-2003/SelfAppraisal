package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    List<Employee> findByCompanyCompanyId(Long companyId);

    List<Employee> findByCompanyCompanyId(int companyId);

    List<Employee> findByIsReportingManagerTrue();

    List<Employee> findByReportingTo(Employee manager);
//
//    Optional<Employee> findByEmail(String email);

    List<Employee> findByReportingToAndIsActiveTrue(Employee manager);

    List<Employee> findByIsActiveTrue();

    List<Employee> findByDepartmentDepartmentId(Integer departmentId);

    Employee findByEmail(String email);

}