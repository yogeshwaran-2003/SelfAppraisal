package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.AppraisalCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppraisalCycleRepository
        extends JpaRepository<AppraisalCycle, Long> {

    List<AppraisalCycle> findByStatus(String status);

    boolean existsByStatus(String status);

}