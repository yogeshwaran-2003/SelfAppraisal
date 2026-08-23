package com.yogesh.selfappraisal.controller;

import jakarta.servlet.http.HttpSession;
import com.yogesh.selfappraisal.entity.Department;
import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.DepartmentRepository;
import com.yogesh.selfappraisal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentsController {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private UserRepository userRepo;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String departments(Model model){
        model.addAttribute("departments", departmentRepo.findAll());
        model.addAttribute("content", "dashboard-pages/departments");
        return "dashboard";
    }

    @GetMapping("/create")
    public String createDepartment(Model model){
        model.addAttribute( "content","dashboard-pages/department-form");
        return "dashboard";
    }

    @PostMapping("/save")
    public String saveDepartment(@RequestParam String departmentCode,
                                 @RequestParam String departmentName,
                                 @RequestParam String description) {

        // 🔐 Get logged-in user from Spring Security
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        Department d = new Department();
        d.setDepartmentCode(departmentCode);
        d.setDepartmentName(departmentName);
        d.setDescription(description);
        d.setActive(true);
        d.setCompany(user.getCompany());

        departmentRepo.save(d);

        return "redirect:/departments";
    }

    @GetMapping("/view/{id}")
    public String viewDepartment(@PathVariable int id, Model model){
        Department department = departmentRepo.findById(id).orElse(null);
        model.addAttribute("department", department);
        model.addAttribute("content", "dashboard-pages/department-view");
        return "/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable int id, Model model){
        Department department = departmentRepo.findById(id).orElse(null);
        model.addAttribute("department", department);
        model.addAttribute("content", "dashboard-pages/department-edit");
        return "dashboard";
    }

    @PostMapping("/update")
    public String updateDepartment(@RequestParam int departmentId,
                                   @RequestParam String departmentCode,
                                   @RequestParam String departmentName,
                                   @RequestParam String description) {

        Department d = departmentRepo.findById(departmentId).orElse(null);

        // 🔐 Get logged-in user from Spring Security
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        if (d != null) {
            d.setDepartmentCode(departmentCode);
            d.setDepartmentName(departmentName);
            d.setDescription(description);

            if (user != null) {
                d.setUpdatedBy((long) user.getUserId());
            }

            departmentRepo.save(d);
        }

        return "redirect:/departments";
    }

    @GetMapping("/toggle/{id}")
    public String toggleDepartment(@PathVariable int id){

        Department d = departmentRepo.findById(id).orElse(null);

        if(d != null){
            d.setActive(!d.isActive());   // toggle ACTIVE / INACTIVE
            departmentRepo.save(d);
        }

        return "redirect:/departments";
    }
}