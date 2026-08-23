package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.*;
import com.yogesh.selfappraisal.repository.UserRepository;
import com.yogesh.selfappraisal.service.AttendanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/manager/attendance")
@PreAuthorize("hasRole('MANAGER')")
public class AttenManagerController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public AttenManagerController(AttendanceService attendanceService,
                                  UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    // ============================
    // GET LOGGED-IN USER SAFELY
    // ============================
    private User getLoggedInUser() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username);
    }

    // ============================
    // LOAD PAGE
    // ============================
    @GetMapping
    public String loadAttendance(@RequestParam(required = false) String date,
                                 Model model) {

        User user = getLoggedInUser();

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        LocalDate selectedDate = (date == null)
                ? LocalDate.now()
                : LocalDate.parse(date);

        List<Employee> team =
                attendanceService.getManagerTeam(user.getEmployee());

        Map<Long, AttendanceStatus> existingMap =
                attendanceService.getAttendanceForDate(team, selectedDate);

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("employees", team);
        model.addAttribute("statuses", AttendanceStatus.values());
        model.addAttribute("existingMap", existingMap);
        model.addAttribute("content", "dashboard-pages/attendance-manager");

        return "dashboard";
    }

    // ============================
    // SAVE
    // ============================
    @PostMapping("/save")
    public String saveAttendance(@RequestParam String date,
                                 @RequestParam Map<String, String> params) {

        User user = getLoggedInUser();

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        LocalDate selectedDate = LocalDate.parse(date);

        Map<Long, AttendanceStatus> attendanceMap = new HashMap<>();

        for (String key : params.keySet()) {

            if (key.startsWith("emp_")) {

                Long empId = Long.parseLong(key.replace("emp_", ""));
                AttendanceStatus status =
                        AttendanceStatus.valueOf(params.get(key));

                attendanceMap.put(empId, status);
            }
        }

        attendanceService.saveAttendance(selectedDate, attendanceMap, user);

        return "redirect:/manager/attendance?date=" + selectedDate;
    }
}