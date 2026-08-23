package com.yogesh.selfappraisal.repository;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findByManager(Employee manager);

}