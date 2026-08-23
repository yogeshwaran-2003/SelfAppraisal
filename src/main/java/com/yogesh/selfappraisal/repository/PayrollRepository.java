package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.PayrollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<PayrollTransaction, Long> {

    // ============================
    // PREVENT DUPLICATE PAYROLL
    // ============================
    Optional<PayrollTransaction> findByEmployeeAndMonthAndYear(
            Employee employee,
            Integer month,
            Integer year
    );

    // ============================
    // ADMIN VIEW
    // ============================
    List<PayrollTransaction> findByMonthAndYear(
            Integer month,
            Integer year
    );

    // ============================
    // EMPLOYEE PAYSLIP VIEW
    // ============================
    List<PayrollTransaction> findByEmployeeOrderByYearDescMonthDesc(
            Employee employee
    );

    List<PayrollTransaction> findByMonthAndYear(int month, int year);

    List<PayrollTransaction> findByEmployeeEmployeeId(Long employeeId);

    List<PayrollTransaction> findByEmployeeReportingToEmployeeId(Long managerId);
}