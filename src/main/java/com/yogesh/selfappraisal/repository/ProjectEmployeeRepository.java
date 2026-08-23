package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Project;
import com.yogesh.selfappraisal.entity.ProjectEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectEmployeeRepository extends JpaRepository<ProjectEmployee, Long> {

    List<ProjectEmployee> findByProject(Project project);

}