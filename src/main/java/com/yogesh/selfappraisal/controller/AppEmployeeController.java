package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.*;
import com.yogesh.selfappraisal.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

import java.time.LocalDate;
import java.util.*;

@Controller
@PreAuthorize("hasRole('EMPLOYEE')")
public class AppEmployeeController {

    private final AppraisalCycleRepository appraisalCycleRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final GoalReviewRepository goalReviewRepository;
    private final SelfAppraisalRepository selfAppraisalRepository;

    public AppEmployeeController(
            AppraisalCycleRepository appraisalCycleRepository,
            UserRepository userRepository,
            GoalRepository goalRepository,
            GoalReviewRepository goalReviewRepository,
            SelfAppraisalRepository selfAppraisalRepository) {

        this.appraisalCycleRepository = appraisalCycleRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.goalReviewRepository = goalReviewRepository;
        this.selfAppraisalRepository = selfAppraisalRepository;
    }

    //my self appraisal
    @GetMapping("/appraisal/employee/self")
    public String openSelfAppraisal(Model model,
                                    Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        Employee employee = user.getEmployee();

        // Get OPEN cycle
        AppraisalCycle cycle = appraisalCycleRepository.findAll()
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .findFirst()
                .orElse(null);

        if (cycle == null) {
            return "redirect:/appraisal";
        }

        // Load appraisal
        SelfAppraisal appraisal =
                selfAppraisalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employee.getEmployeeId(),
                                cycle.getId())
                        .orElse(null);

        if (appraisal == null) {

            appraisal = new SelfAppraisal();
            appraisal.setEmployee(employee);
            appraisal.setAppraisalCycle(cycle);
            appraisal.setStatus("DRAFT");

            appraisal = selfAppraisalRepository.save(appraisal);
        }

        // Load Goals
        List<Goal> goals =
                goalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employee.getEmployeeId(),
                                cycle.getId());

        // Load Reviews
        Map<Long, GoalReview> reviewMap = new HashMap<>();

        for (Goal goal : goals) {

            GoalReview review =
                    goalReviewRepository
                            .findByGoalId(goal.getId())
                            .stream()
                            .findFirst()
                            .orElseGet(() -> {

                                GoalReview newReview = new GoalReview();
                                newReview.setGoal(goal);

                                return goalReviewRepository.save(newReview);
                            });

            reviewMap.put(goal.getId(), review);
        }

        // ===== LOCK FORM IF SUBMITTED OR REVIEWED =====

        boolean locked =
                "SUBMITTED".equals(appraisal.getStatus()) ||
                        "REVIEWED".equals(appraisal.getStatus());

        model.addAttribute("cycle", cycle);
        model.addAttribute("goals", goals);
        model.addAttribute("reviews", reviewMap);
        model.addAttribute("appraisal", appraisal);
        model.addAttribute("locked", locked);
        model.addAttribute("content", "dashboard-pages/employee-self");

        return "dashboard";
    }
//    @GetMapping("/appraisal/employee/self")
//    public String openSelfAppraisal(Model model,
//                                    Authentication authentication) {
//
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//
//        if (user == null || user.getEmployee() == null) {
//            return "redirect:/dashboard";
//        }
//
//        Employee employee = user.getEmployee();
//
//        // 1️⃣ Get OPEN cycle
//        AppraisalCycle cycle = appraisalCycleRepository.findAll()
//                .stream()
//                .filter(c -> "OPEN".equals(c.getStatus()))
//                .findFirst()
//                .orElse(null);
//
//        if (cycle == null) {
//            return "redirect:/appraisal";
//        }
//
//        // 2️⃣ Get or Create SelfAppraisal
//        SelfAppraisal appraisal =
//                selfAppraisalRepository
//                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
//                                employee.getEmployeeId(),
//                                cycle.getId())
//                        .orElseGet(() -> {
//                            SelfAppraisal newAppraisal = new SelfAppraisal();
//                            newAppraisal.setEmployee(employee);
//                            newAppraisal.setAppraisalCycle(cycle);
//                            newAppraisal.setStatus("DRAFT");
//                            return selfAppraisalRepository.save(newAppraisal);
//                        });
//
//        // 3️⃣ Load Goals (ONLY for this cycle)
//        List<Goal> goals =
//                goalRepository
//                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
//                                employee.getEmployeeId(),
//                                cycle.getId());
//
//        // 4️⃣ Load or Create GoalReviews
//        Map<Long, GoalReview> reviewMap = new HashMap<>();
//
//        for (Goal goal : goals) {
//
//            GoalReview review =
//                    goalReviewRepository
//                            .findByGoalId(goal.getId())
//                            .stream()
//                            .findFirst()
//                            .orElseGet(() -> {
//                                GoalReview newReview = new GoalReview();
//                                newReview.setGoal(goal);
//                                return goalReviewRepository.save(newReview);
//                            });
//
//            reviewMap.put(goal.getId(), review);
//        }
//
//        // 5️⃣ Lock if already submitted
//        boolean locked = "SUBMITTED".equals(appraisal.getStatus());
//
//        model.addAttribute("cycle", cycle);
//        model.addAttribute("goals", goals);
//        model.addAttribute("reviews", reviewMap);
//        model.addAttribute("appraisal", appraisal);
//        model.addAttribute("locked", locked);
//        model.addAttribute("content",
//                "dashboard-pages/employee-self");
//
//        return "dashboard";
//    }

    //my self appraisal
    @PostMapping("/appraisal/employee/self/save")
    public String saveSelfAppraisal(
            @RequestParam Long cycleId,
            @RequestParam String action,
            @RequestParam(required = false) String projectSummary,
            @RequestParam(required = false) String taskSummary,
            @RequestParam(required = false) String achievements,
            @RequestParam(required = false) String strengths,
            @RequestParam(required = false) String improvements,
            @RequestParam(required = false) String learning,
            @RequestParam(required = false) String challenges,
            @RequestParam(required = false) String futureGoals,
            HttpServletRequest request,
            @RequestParam Map<String, MultipartFile> files,
            Authentication authentication) {

        try {

            String username = authentication.getName();
            User user = userRepository.findByUsername(username);

            if (user == null || user.getEmployee() == null) {
                return "redirect:/dashboard";
            }

            Employee employee = user.getEmployee();

            AppraisalCycle cycle =
                    appraisalCycleRepository.findById(cycleId).orElse(null);

            if (cycle == null) {
                return "redirect:/appraisal";
            }

            SelfAppraisal appraisal =
                    selfAppraisalRepository
                            .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                    employee.getEmployeeId(), cycleId)
                            .orElse(new SelfAppraisal());

            appraisal.setEmployee(employee);
            appraisal.setAppraisalCycle(cycle);

            // ===== SAVE COMMON FIELDS =====

            appraisal.setProjectSummary(projectSummary);
            appraisal.setTaskSummary(taskSummary);
            appraisal.setAchievements(achievements);
            appraisal.setStrengths(strengths);
            appraisal.setImprovements(improvements);
            appraisal.setLearning(learning);
            appraisal.setChallenges(challenges);
            appraisal.setFutureGoals(futureGoals);

            // ===== LOAD GOALS =====

            List<Goal> goals =
                    goalRepository
                            .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                    employee.getEmployeeId(), cycleId);

            // ===== CORRECT FILE UPLOAD PATH =====

            String uploadDir = System.getProperty("user.dir") + "/uploads/goals/";
            File directory = new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // ===== PROCESS EACH GOAL =====

            for (Goal goal : goals) {

                String scoreParam = request.getParameter("score_" + goal.getId());
                String commentParam = request.getParameter("comment_" + goal.getId());

                MultipartFile file = files.get("file_" + goal.getId());

                Integer score = null;

                if (scoreParam != null && !scoreParam.isBlank()) {
                    score = Integer.parseInt(scoreParam);
                }

                GoalReview review =
                        goalReviewRepository
                                .findByGoalId(goal.getId())
                                .stream()
                                .findFirst()
                                .orElse(new GoalReview());

                review.setGoal(goal);
                review.setSelfScore(score);
                review.setSelfComment(commentParam);

                // ===== HANDLE FILE UPLOAD =====

                if (file != null && !file.isEmpty()) {

                    String fileName =
                            System.currentTimeMillis()
                                    + "_"
                                    + file.getOriginalFilename();

                    File destination = new File(uploadDir + fileName);

                    file.transferTo(destination);

                    review.setEvidenceFile(fileName);
                }

                goalReviewRepository.save(review);
            }

            // ===== HANDLE SUBMIT =====

            if ("submit".equals(action)) {

                int total = 0;
                int count = 0;

                for (Goal goal : goals) {

                    GoalReview review =
                            goalReviewRepository
                                    .findByGoalId(goal.getId())
                                    .stream()
                                    .findFirst()
                                    .orElse(null);

                    if (review != null && review.getSelfScore() != null) {

                        total += review.getSelfScore();
                        count++;
                    }
                }

                if (count > 0) {

                    double average = (double) total / count;

                    int rounded = (int) Math.round(average);

                    appraisal.setOverallSelfRating(rounded);
                }

                appraisal.setStatus("SUBMITTED");
                appraisal.setSubmittedDate(java.time.LocalDate.now());

            } else {

                appraisal.setStatus("DRAFT");
            }

            selfAppraisalRepository.save(appraisal);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "redirect:/appraisal/employee/self";
    }

//    @PostMapping("/appraisal/employee/self/save")
//    public String saveSelfAppraisal(
//            @RequestParam Long cycleId,
//            @RequestParam String action,
//            @RequestParam(required = false) String projectSummary,
//            @RequestParam(required = false) String taskSummary,
//            @RequestParam(required = false) String achievements,
//            @RequestParam(required = false) String strengths,
//            @RequestParam(required = false) String improvements,
//            @RequestParam(required = false) String learning,
//            @RequestParam(required = false) String challenges,
//            @RequestParam(required = false) String futureGoals,
//            HttpServletRequest request,
//            @RequestParam Map<String, MultipartFile> files,
//            Authentication authentication) {
//
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//
//        if (user == null || user.getEmployee() == null) {
//            return "redirect:/dashboard";
//        }
//
//        Employee employee = user.getEmployee();
//
//        AppraisalCycle cycle =
//                appraisalCycleRepository.findById(cycleId).orElse(null);
//
//        if (cycle == null) {
//            return "redirect:/appraisal";
//        }
//
//        SelfAppraisal appraisal =
//                selfAppraisalRepository
//                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
//                                employee.getEmployeeId(), cycleId)
//                        .orElse(new SelfAppraisal());
//
//        appraisal.setEmployee(employee);
//        appraisal.setAppraisalCycle(cycle);
//
//        // ================= SAVE NEW COMMON FIELDS =================
//        appraisal.setProjectSummary(projectSummary);
//        appraisal.setTaskSummary(taskSummary);
//        appraisal.setAchievements(achievements);
//        appraisal.setStrengths(strengths);
//        appraisal.setImprovements(improvements);
//        appraisal.setLearning(learning);
//        appraisal.setChallenges(challenges);
//        appraisal.setFutureGoals(futureGoals);
//
//        // ================= HANDLE GOAL REVIEWS =================
//        List<Goal> goals =
//                goalRepository
//                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
//                                employee.getEmployeeId(), cycleId);
//
//        for (Goal goal : goals) {
//
//            String scoreParam = request.getParameter("score_" + goal.getId());
//            String commentParam = request.getParameter("comment_" + goal.getId());
//
//            MultipartFile file = files.get("file_" + goal.getId());
//
//            Integer score = null;
//            if (scoreParam != null && !scoreParam.isBlank()) {
//                score = Integer.parseInt(scoreParam);
//            }
//
//            GoalReview review =
//                    goalReviewRepository
//                            .findByGoalId(goal.getId())
//                            .stream()
//                            .findFirst()
//                            .orElse(new GoalReview());
//
//            review.setGoal(goal);
//            review.setSelfScore(score);
//            review.setSelfComment(commentParam);
//
//            // ===== FILE UPLOAD =====
//            if (file != null && !file.isEmpty()) {
//
//                try {
//
//                    String uploadDir = "uploads/goals/";
//                    new File(uploadDir).mkdirs();
//
//                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//                    file.transferTo(new File(uploadDir + fileName));
//
//                    review.setEvidenceFile(fileName);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            goalReviewRepository.save(review);
//        }
//
//        // ================= HANDLE SUBMIT =================
//        if ("submit".equals(action)) {
//
//            int total = 0;
//            int count = 0;
//
//            for (Goal goal : goals) {
//
//                GoalReview review =
//                        goalReviewRepository
//                                .findByGoalId(goal.getId())
//                                .stream()
//                                .findFirst()
//                                .orElse(null);
//
//                if (review != null && review.getSelfScore() != null) {
//                    total += review.getSelfScore();
//                    count++;
//                }
//            }
//
//            if (count > 0) {
//                double average = (double) total / count;
//                int rounded = (int) Math.round(average);
//                appraisal.setOverallSelfRating(rounded);
//            }
//
//            appraisal.setStatus("SUBMITTED");
//            appraisal.setSubmittedDate(
//                    java.time.LocalDate.now());
//
//        } else {
//            appraisal.setStatus("DRAFT");
//        }
//
//        selfAppraisalRepository.save(appraisal);
//
//        return "redirect:/appraisal/employee/self";
//    }


    //my goals
    @GetMapping("/appraisal/employee/goals")
    public String employeeGoals(Model model,
                                Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        Employee employee = user.getEmployee();

        // Find OPEN cycle
        AppraisalCycle cycle = appraisalCycleRepository.findAll()
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .findFirst()
                .orElse(null);

        if (cycle == null) {
            return "redirect:/appraisal";
        }

        List<Goal> goals =
                goalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employee.getEmployeeId(),
                                cycle.getId());

        SelfAppraisal appraisal =
                selfAppraisalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employee.getEmployeeId(),
                                cycle.getId())
                        .orElse(null);

        boolean locked = appraisal != null &&
                "SUBMITTED".equals(appraisal.getStatus());

        model.addAttribute("locked", locked);

        model.addAttribute("cycle", cycle);
        model.addAttribute("goals", goals);
        model.addAttribute("content",
                "dashboard-pages/goal-list");

        return "dashboard";
    }

    //my goals
    @GetMapping("/appraisal/employee/goals/create")
    public String createGoal(Model model,
                             Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        AppraisalCycle cycle = appraisalCycleRepository.findAll()
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .findFirst()
                .orElse(null);

        if (cycle == null) {
            return "redirect:/appraisal";
        }

        model.addAttribute("goal", new Goal());
        model.addAttribute("cycle", cycle);
        model.addAttribute("content",
                "dashboard-pages/goal-create");

        return "dashboard";
    }

    //my goals
    @PostMapping("/appraisal/employee/goals/create")
    public String createGoal(Goal goal,
                             Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        Employee employee = user.getEmployee();

        AppraisalCycle cycle = appraisalCycleRepository.findAll()
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .findFirst()
                .orElse(null);

        if (cycle == null) {
            return "redirect:/appraisal";
        }

        goal.setEmployee(employee);
        goal.setAppraisalCycle(cycle);
        goal.setStatus("ACTIVE");

        goalRepository.save(goal);

        return "redirect:/appraisal/employee/goals";
    }

    //my goals
    @GetMapping("/appraisal/employee/goals/edit/{id}")
    public String editGoal(@PathVariable Long id,
                           Model model) {

        Goal goal = goalRepository.findById(id).orElse(null);

        if (goal == null) {
            return "redirect:/appraisal/employee/goals";
        }

        model.addAttribute("goal", goal);
        model.addAttribute("content",
                "dashboard-pages/goal-edit");

        return "dashboard";
    }

    //my goals
    @PostMapping("/appraisal/employee/goals/update")
    public String updateGoal(@RequestParam Long id,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam Integer weightage,
                             Authentication authentication) {

        // 1️⃣ Get logged in user
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null || user.getEmployee() == null) {
            return "redirect:/dashboard";
        }

        Employee employee = user.getEmployee();

        // 2️⃣ Get OPEN cycle
        AppraisalCycle cycle = appraisalCycleRepository.findAll()
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .findFirst()
                .orElse(null);

        if (cycle == null) {
            return "redirect:/appraisal";
        }

        // 3️⃣ Check SelfAppraisal status
        SelfAppraisal appraisal =
                selfAppraisalRepository
                        .findByEmployeeEmployeeIdAndAppraisalCycleId(
                                employee.getEmployeeId(),
                                cycle.getId())
                        .orElse(null);

        if (appraisal != null &&
                "SUBMITTED".equals(appraisal.getStatus())) {

            // 🚫 STOP editing
            return "redirect:/appraisal/employee/goals";
        }

        // 4️⃣ Now safe to update
        Goal goal = goalRepository.findById(id).orElse(null);

        if (goal != null) {
            goal.setTitle(title);
            goal.setDescription(description);
            goal.setWeightage(weightage);
            goalRepository.save(goal);
        }

        return "redirect:/appraisal/employee/goals";
    }

    //my goals
    @GetMapping("/appraisal/employee/goals/delete/{id}")
    public String deleteGoal(@PathVariable Long id) {

        goalRepository.deleteById(id);

        return "redirect:/appraisal/employee/goals";
    }

    @GetMapping("/appraisal/instructions")
    public String viewInstructions(Model model) {

        model.addAttribute("content",
                "dashboard-pages/instruction");

        return "dashboard";
    }

}