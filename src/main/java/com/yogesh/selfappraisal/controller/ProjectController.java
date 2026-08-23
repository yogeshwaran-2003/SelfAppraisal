package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.Project;
import com.yogesh.selfappraisal.entity.ProjectEmployee;
import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.ProjectEmployeeRepository;
import com.yogesh.selfappraisal.repository.ProjectRepository;
import com.yogesh.selfappraisal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;
    private final EmployeeRepository employeeRepo;

    @Autowired
    private ProjectEmployeeRepository projectEmployeeRepo;

    public ProjectController(ProjectRepository projectRepo,
                             UserRepository userRepo,
                             EmployeeRepository employeeRepo) {

        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
    }


    // ADMIN + MANAGER
    @GetMapping("/projects")
    public String projects(Model model){

        List<Project> projects = projectRepo.findAll();

        model.addAttribute("projects",projects);
        model.addAttribute("content","dashboard-pages/projects");

        return "dashboard";
    }


    // CREATE PAGE
    @GetMapping("/projects/create")
    public String createProject(Model model){

        model.addAttribute("project",new Project());
        model.addAttribute("managers",employeeRepo.findByIsReportingManagerTrue());
        model.addAttribute("employees", employeeRepo.findByIsActiveTrue());
        model.addAttribute("content","dashboard-pages/project-form");

        return "dashboard";
    }


    // SAVE PROJECT
//    @PostMapping("/projects/save")
//    public String saveProject(Project project){
//
//        projectRepo.save(project);
//
//        return "redirect:/projects";
//    }

    @PostMapping("/projects/save")
    public String saveProject(Project project,
                              @RequestParam(required = false) List<Long> employeeIds){

        projectRepo.save(project);

        if(employeeIds != null){

            for(Long empId : employeeIds){

                Employee emp =
                        employeeRepo.findById(empId).orElse(null);

                ProjectEmployee pe = new ProjectEmployee();

                pe.setProject(project);
                pe.setEmployee(emp);

                projectEmployeeRepo.save(pe);
            }

        }

        return "redirect:/projects";
    }


    // MANAGER PROJECT VIEW

//    @GetMapping("/projects/manager")
//    public String managerProjects(Model model){
//
//        Authentication auth =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        String username = auth.getName();
//
//        User user = userRepo.findByUsername(username);
//
//        Employee manager =
//                employeeRepo.findById(user.getEmployee().getEmployeeId())
//                        .orElse(null);
//
//        List<Project> projects =
//                projectRepo.findByManager(manager);
//
//        model.addAttribute("projects",projects);
//        model.addAttribute("content","dashboard-pages/projects-manager");
//
//        return "dashboard";
//    }

//    @GetMapping("/projects/manager")
//    public String managerProjects(Model model, Authentication auth){
//
//        String username = auth.getName();
//
//        User user = userRepo.findByUsername(username);
//
//        Employee manager = employeeRepo.findByEmail(user.getUsername());
//
//        List<Project> projects = projectRepo.findByManager(manager);
//
//        model.addAttribute("projects", projects);
//        model.addAttribute("content", "dashboard-pages/projects-manager");
//
//        return "dashboard";
//    }
}