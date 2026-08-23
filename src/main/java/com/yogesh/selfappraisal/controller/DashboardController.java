package com.yogesh.selfappraisal.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import com.yogesh.selfappraisal.entity.Department;
import com.yogesh.selfappraisal.repository.DepartmentRepository;
import com.yogesh.selfappraisal.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            HttpServletResponse response){

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        model.addAttribute("content", "dashboard-pages/home");
        return "dashboard";
    }

    //DASHBOARD HOME
    @GetMapping("/dashboard/home")
    public String home(){
        return "dashboard-pages/home";
    }

}