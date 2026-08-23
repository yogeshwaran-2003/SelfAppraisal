package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.SelfAppraisal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SelfAppraisalRepository
        extends JpaRepository<SelfAppraisal, Long> {

    Optional<SelfAppraisal>
    findByEmployeeEmployeeIdAndAppraisalCycleId(
            Long employeeId, Long cycleId);

    Optional<SelfAppraisal> findTopByEmployeeAndStatusOrderByFinalizedDateDesc(
            Employee employee,
            String status
    );

    List<SelfAppraisal> findByStatus(String status);
}