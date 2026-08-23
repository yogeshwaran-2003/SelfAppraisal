package com.yogesh.selfappraisal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yogesh.selfappraisal.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
