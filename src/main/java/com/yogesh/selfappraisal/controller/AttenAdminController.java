package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.service.AttendanceService;
import com.yogesh.selfappraisal.service.AdminAttendanceRow;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/attendance/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AttenAdminController {

    private final AttendanceService attendanceService;

    public AttenAdminController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String viewAdminAttendance(@RequestParam(required = false) Integer month,
                                      @RequestParam(required = false) Integer year,
                                      Model model) {

        YearMonth current = YearMonth.now();

        int selectedMonth = (month == null) ? current.getMonthValue() : month;
        int selectedYear = (year == null) ? current.getYear() : year;

        List<AdminAttendanceRow> report =
                attendanceService.getMonthlyAdminReport(selectedMonth, selectedYear);

        model.addAttribute("month", selectedMonth);
        model.addAttribute("year", selectedYear);
        model.addAttribute("report", report);
        model.addAttribute("content", "dashboard-pages/attendance-admin");

        return "dashboard";
    }
}