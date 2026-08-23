package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.*;
import com.yogesh.selfappraisal.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yogesh.selfappraisal.repository.GoalRepository;
import com.yogesh.selfappraisal.repository.GoalReviewRepository;
import com.yogesh.selfappraisal.repository.SelfAppraisalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasRole('MANAGER')")
public class AppManagerController {

    private final AppraisalCycleRepository appraisalCycleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;


    private final GoalRepository goalRepository;
    private final GoalReviewRepository goalReviewRepository;
    private final SelfAppraisalRepository selfAppraisalRepository;

    public AppManagerController(AppraisalCycleRepository appraisalCycleRepository,
                                EmployeeRepository employeeRepository,
                                UserRepository userRepository,
                                GoalRepository goalRepository,
                                GoalReviewRepository goalReviewRepository,
                                SelfAppraisalRepository selfAppraisalRepository) {
        this.appraisalCycleRepository = appraisalCycleRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.goalReviewRepository = goalReviewRepository;
        this.selfAppraisalRepository = selfAppraisalRepository;
    }

    @GetMapping("/appraisal/manager/reviews")
    public String managerReviews(Model model,
                                 Authentication authentication,
                                 @RequestParam(required = false) Long cycleId) {

        // Load OPEN cycles
        List<AppraisalCycle> openCycles =
                appraisalCycleRepository.findAll()
                        .stream()
                        .filter(c -> "OPEN".equals(c.getStatus()))
                        .toList();

        model.addAttribute("cycles", openCycles);

        if (cycleId != null) {

            // 🔹 ADD THIS PART
            AppraisalCycle selectedCycle =
                    appraisalCycleRepository.findById(cycleId).orElse(null);

            model.addAttribute("cycle", selectedCycle);

            String username = authentication.getName();
            User user = userRepository.findByUsername(username);

            if (user != null && user.getEmployee() != null) {

                Employee manager = user.getEmployee();

                List<Employee> team =
                        employeeRepository.findByReportingTo(manager);

                Map<Long, String> appraisalStatusMap = new HashMap<>();

                for (Employee emp : team) {

                    SelfAppraisal appraisal =
                            selfAppraisalRepository
                                    .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                            emp.getEmployeeId(), cycleId)
                                    .orElse(null);

                    if (appraisal != null) {
                        appraisalStatusMap.put(emp.getEmployeeId(), appraisal.getStatus());
                    } else {
                        appraisalStatusMap.put(emp.getEmployeeId(), "NOT_SUBMITTED");
                    }
                }

                model.addAttribute("team", team);
                model.addAttribute("appraisalStatusMap", appraisalStatusMap);
            }
        }

        model.addAttribute("content",
                "dashboard-pages/appraisal-manager");

        return "dashboard";
    }

    @GetMapping("/appraisal/manager/review")
    public String reviewEmployee(Model model,
                                 @RequestParam Long employeeId,
                                 @RequestParam Long cycleId) {

        SelfAppraisal appraisal =
                selfAppraisalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employeeId, cycleId)
                        .orElse(null);

        if (appraisal == null ||
                !"SUBMITTED".equals(appraisal.getStatus())
                        && !"REVIEWED".equals(appraisal.getStatus())) {

            return "redirect:/appraisal/manager/reviews";
        }

        List<Goal> goals =
                goalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employeeId, cycleId);

        Map<Long, GoalReview> reviewMap = new HashMap<>();

        for (Goal goal : goals) {

            GoalReview review =
                    goalReviewRepository
                            .findByGoalId(goal.getId())
                            .orElseGet(() -> {
                                GoalReview newReview = new GoalReview();
                                newReview.setGoal(goal);
                                return goalReviewRepository.save(newReview);
                            });

            reviewMap.put(goal.getId(), review);
        }

        boolean isReviewed = "REVIEWED".equals(appraisal.getStatus());

        model.addAttribute("goals", goals);
        model.addAttribute("reviews", reviewMap);
        model.addAttribute("appraisal", appraisal);
        model.addAttribute("isReviewed", isReviewed);
        model.addAttribute("content",
                "dashboard-pages/manager-review");

        return "dashboard";
    }

    @PostMapping("/appraisal/manager/review/save")
    public String saveManagerReview(
            @RequestParam Long employeeId,
            @RequestParam Long cycleId,
            @RequestParam(required = false) Integer overallManagerRating,
            @RequestParam(required = false) String overallManagerComment,
            HttpServletRequest request) {

        // 1️⃣ Load SelfAppraisal
        SelfAppraisal appraisal =
                selfAppraisalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employeeId, cycleId)
                        .orElse(null);

        if (appraisal == null ||
                !"SUBMITTED".equals(appraisal.getStatus())) {
            return "redirect:/appraisal/manager/reviews?cycleId=" + cycleId;
        }

        // 2️⃣ Load goals
        List<Goal> goals =
                goalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employeeId, cycleId);

        // 3️⃣ Save manager score for each goal
        for (Goal goal : goals) {

            String scoreParam =
                    request.getParameter("managerScore_" + goal.getId());

            Integer managerScore = null;

            if (scoreParam != null && !scoreParam.isBlank()) {
                managerScore = Integer.parseInt(scoreParam);
            }

            GoalReview review =
                    goalReviewRepository
                            .findByGoalId(goal.getId())
                            .stream()
                            .findFirst()
                            .orElse(null);

            if (review != null) {
                review.setManagerScore(managerScore);
                goalReviewRepository.save(review);
            }
        }

        // 4️⃣ Save overall manager rating + comment
        appraisal.setOverallManagerRating(overallManagerRating);
        appraisal.setOverallManagerComment(overallManagerComment);

        // 5️⃣ Update status
        appraisal.setStatus("REVIEWED");
        appraisal.setReviewedDate(
                java.time.LocalDate.now());

        selfAppraisalRepository.save(appraisal);

        return "redirect:/appraisal/manager/reviews?cycleId=" + cycleId;
    }
}