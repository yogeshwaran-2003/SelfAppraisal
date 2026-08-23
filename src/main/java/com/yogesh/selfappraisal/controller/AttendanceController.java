package com.yogesh.selfappraisal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @GetMapping
    public String attendanceHome(Model model) {

        model.addAttribute("content", "dashboard-pages/attendance");

        return "dashboard";
    }
}