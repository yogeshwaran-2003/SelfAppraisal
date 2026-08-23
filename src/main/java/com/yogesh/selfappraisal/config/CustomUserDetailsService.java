package com.yogesh.selfappraisal.config;

import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class
CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("Login attempt for: " + username);

        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Check account status
        if (!"ACTIVE".equalsIgnoreCase(user.getAccountStatus())) {
            throw new UsernameNotFoundException("User account is inactive");
        }

        // Safety check for role
        if (user.getRole() == null) {
            throw new UsernameNotFoundException("User has no role assigned");
        }

        String roleName = user.getRole().getRoleName();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),  // BCrypt password from DB
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase())
                )
        );
    }
}