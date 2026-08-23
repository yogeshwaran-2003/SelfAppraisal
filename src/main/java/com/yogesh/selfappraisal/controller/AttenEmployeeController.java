package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Attendance;
import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.UserRepository;
import com.yogesh.selfappraisal.service.AttendanceService;
import com.yogesh.selfappraisal.service.AttendanceSummary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/attendance/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class AttenEmployeeController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public AttenEmployeeController(AttendanceService attendanceService,
                                   UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username);
    }

    @GetMapping
    public String viewAttendance(@RequestParam(required = false) Integer month,
                                 @RequestParam(required = false) Integer year,
                                 Model model) {

        User user = getLoggedInUser();
        if (user == null || user.getEmployee() == null)
            return "redirect:/dashboard";

        YearMonth current = YearMonth.now();

        int selectedMonth = (month == null) ? current.getMonthValue() : month;
        int selectedYear = (year == null) ? current.getYear() : year;

        AttendanceSummary summary =
                attendanceService.getMonthlySummary(
                        user.getEmployee(),
                        selectedMonth,
                        selectedYear
                );

        List<Attendance> records =
                attendanceService.getMonthlyRecords(
                        user.getEmployee(),
                        selectedMonth,
                        selectedYear
                );

        model.addAttribute("records", records);
        model.addAttribute("month", selectedMonth);
        model.addAttribute("year", selectedYear);
        model.addAttribute("summary", summary);
        model.addAttribute("content", "dashboard-pages/attendance-employee");

        return "dashboard";
    }
}