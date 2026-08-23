package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Role;
import com.yogesh.selfappraisal.repository.RoleRepository;
import com.yogesh.selfappraisal.entity.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepo;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String roles(Model model, HttpSession session){

        User user = (User) session.getAttribute("user");

        model.addAttribute("roles", roleRepo.findAll());

        model.addAttribute("content", "dashboard-pages/roles");

        return "dashboard";
    }

    @GetMapping("/create")
    public String createRole(Model model, HttpSession session){

        User user = (User) session.getAttribute("user");

        model.addAttribute("role", new Role());

        model.addAttribute("content", "dashboard-pages/roles-form");

        return "dashboard";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute Role role){

        if(role.getIsActive() == null){
            role.setIsActive(true);
        }

        roleRepo.save(role);

        return "redirect:/roles";
    }

    @GetMapping("/view/{id}")
    public String viewRole(@PathVariable Integer id,
                           Model model,
                           HttpSession session){

        User user = (User) session.getAttribute("user");

        Role role = roleRepo.findById(id).orElse(null);

        model.addAttribute("role", role);

        model.addAttribute("content", "dashboard-pages/roles-view");

        return "dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable Integer id,
                           Model model,
                           HttpSession session){

        User user = (User) session.getAttribute("user");

        Role role = roleRepo.findById(id).orElse(null);

        model.addAttribute("role", role);

        model.addAttribute("content", "dashboard-pages/roles-edit");

        return "dashboard";
    }

    @PostMapping("/update")
    public String updateRole(@ModelAttribute Role role){

        if(role.getIsActive() == null){
            role.setIsActive(false);
        }

        roleRepo.save(role);

        return "redirect:/roles";
    }

    @GetMapping("/toggle/{id}")
    public String toggleRole(@PathVariable Integer id){

        Role role = roleRepo.findById(id).orElse(null);

        if(role != null){
            role.setIsActive(!role.getIsActive());
            roleRepo.save(role);
        }
        return "redirect:/roles";
    }
}