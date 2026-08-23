package com.yogesh.selfappraisal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayrollController {

    @GetMapping("/payroll")
    public String payrollHome(org.springframework.ui.Model model) {

        model.addAttribute("content", "dashboard-pages/payroll.html");

        return "dashboard";
    }
}