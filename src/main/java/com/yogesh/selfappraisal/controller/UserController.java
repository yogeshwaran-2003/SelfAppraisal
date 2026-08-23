package com.yogesh.selfappraisal.controller;
import com.yogesh.selfappraisal.entity.User;

import com.yogesh.selfappraisal.entity.Role;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.RoleRepository;
import com.yogesh.selfappraisal.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String users(Model model, HttpSession session){

        model.addAttribute("users", userRepo.findAll());

        model.addAttribute("content", "dashboard-pages/users");

        return "dashboard";
    }

    @GetMapping("/create")
    public String createUser(Model model, HttpSession session){

        model.addAttribute("user", new User());

        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("employees", employeeRepo.findAll());

        model.addAttribute("content", "dashboard-pages/users-form");

        return "dashboard";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           Authentication authentication) {

        // 1️⃣ Get logged-in username from Spring Security
        String username = authentication.getName();

        User loggedUser = userRepo.findByUsername(username);

        if (loggedUser == null) {
            return "redirect:/login";
        }

        // 2️⃣ Set company automatically
        user.setCompany(loggedUser.getCompany());

        // 3️⃣ Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 4️⃣ Default account status
        if (user.getAccountStatus() == null) {
            user.setAccountStatus("ACTIVE");
        }

        userRepo.save(user);

        return "redirect:/users";
    }

//    @PostMapping("/save")
//    public String saveUser(@ModelAttribute User user,
//                           HttpSession session){
//
//        // get logged in user
//        User loggedUser = (User) session.getAttribute("user");
//
//        // set company automatically
//        user.setCompany(loggedUser.getCompany());
//
//        // encrypt password using BCrypt
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // set default account status
//        if(user.getAccountStatus() == null){
//            user.setAccountStatus("ACTIVE");
//        }
//
//        userRepo.save(user);
//
//        return "redirect:/users";
//    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Integer id,
                           Model model,
                           HttpSession session){

        User user = userRepo.findById(id).orElse(null);

        model.addAttribute("user", user);

        model.addAttribute("content", "dashboard-pages/users-view");

        return "dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id,
                           Model model,
                           HttpSession session){

        User user = userRepo.findById(id).orElse(null);

        model.addAttribute("user", user);

        model.addAttribute("roles", roleRepo.findAll());

        model.addAttribute("content", "dashboard-pages/users-edit");

        return "dashboard";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User formUser,
                             HttpSession session){

        User existingUser = userRepo.findById(formUser.getUserId()).orElse(null);

        if(existingUser == null)
            return "redirect:/users";

        // preserve company
        existingUser.setCompany(existingUser.getCompany());

        // update username
        existingUser.setUsername(formUser.getUsername());

        // update role
        existingUser.setRole(formUser.getRole());

        // update account status
        existingUser.setAccountStatus(formUser.getAccountStatus());

        // update password only if entered
        if(formUser.getPassword() != null &&
                !formUser.getPassword().isBlank()){

            existingUser.setPassword(
                    passwordEncoder.encode(formUser.getPassword())
            );
        }

        userRepo.save(existingUser);

        return "redirect:/users";
    }

    @GetMapping("/toggle/{id}")
    public String toggleUser(@PathVariable Integer id){

        @Nullable Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User loggedUser = userRepo.findByUsername(username);

        if(loggedUser == null){
            return "redirect:/login";
        }

        if(loggedUser.getUserId().equals(id)){
            return "redirect:/users";
        }

        User user = userRepo.findById(id).orElse(null);

        if(user != null){
            if("ACTIVE".equals(user.getAccountStatus())){
                user.setAccountStatus("INACTIVE");
            }else{
                user.setAccountStatus("ACTIVE");
            }
            userRepo.save(user);
        }
        return "redirect:/users";
    }
}