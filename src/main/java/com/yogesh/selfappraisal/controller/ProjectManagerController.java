package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.Project;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.ProjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProjectManagerController {

    private final ProjectRepository projectRepo;
    private final EmployeeRepository employeeRepo;

    public ProjectManagerController(ProjectRepository projectRepo,
                                    EmployeeRepository employeeRepo) {
        this.projectRepo = projectRepo;
        this.employeeRepo = employeeRepo;
    }

    @GetMapping("/projects/manager")
    public String managerProjects(Model model, Authentication auth) {

        // Logged-in username (Spring Security)
        String username = auth.getName();

        // Get manager employee using email
        Employee manager = employeeRepo.findByEmail(username);

        // Get projects assigned to manager
        List<Project> projects = projectRepo.findByManager(manager);

        model.addAttribute("projects", projects);
        model.addAttribute("content", "dashboard-pages/projects-manager");

        return "dashboard";
    }
}