package com.yogesh.selfappraisal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yogesh.selfappraisal.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}