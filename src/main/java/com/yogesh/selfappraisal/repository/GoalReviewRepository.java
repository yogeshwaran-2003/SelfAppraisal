package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.GoalReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalReviewRepository
        extends JpaRepository<GoalReview, Long> {

    Optional<GoalReview> findByGoalId(Long goalId);

}