package com.yogesh.selfappraisal.controller;

import java.time.LocalDateTime;
import org.springframework.ui.Model;
import com.yogesh.selfappraisal.entity.Role;

import com.yogesh.selfappraisal.entity.Company;
import com.yogesh.selfappraisal.repository.CompanyRepository;
import jakarta.servlet.http.HttpSession;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.UserRepository;
import com.yogesh.selfappraisal.repository.RoleRepository;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;


    //INDEX
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // LOGIN
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    //REGISTER AND COMPANY CREATION
    @GetMapping("/register")
    public String showCompanyRegister(){
        return "company-register";
    }

    @PostMapping("/register")
    public String registerCompany(@RequestParam String companyName,
                                  @RequestParam String companyCode,
                                  @RequestParam String registrationNo,
                                  @RequestParam String sector,
                                  @RequestParam String username,
                                  @RequestParam String password){

        // Save Company
        Company c = new Company();
        c.setCompanyName(companyName);
        c.setCompanyCode(companyCode);
        c.setRegistrationNo(registrationNo);
        c.setSector(sector);

        Company savedCompany = companyRepo.save(c);

        // Create First Admin
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setAccountStatus("ACTIVE");
        u.setRole(roleRepo.findById(1).orElse(null)); // ADMIN
        u.setCompany(savedCompany);
        u.setCreatedBy(0L);
        u.setUpdatedBy(0L);

        userRepo.save(u);
        u.setCompany(savedCompany);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}