package com.yogesh.selfappraisal.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "self_appraisal")
public class SelfAppraisal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "appraisal_cycle_id")
    private AppraisalCycle appraisalCycle;

    // ===== Employee Side =====
    private Integer overallSelfRating;

    // ===== Manager Side =====
    private Integer overallManagerRating;

    // ===== Workflow Status =====
    private String status;
    // DRAFT / SUBMITTED / REVIEWED / FINALIZED

    private LocalDate submittedDate;
    private LocalDate reviewedDate;
    private LocalDate finalizedDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String projectSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String taskSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String achievements;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String improvements;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String learning;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String challenges;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String futureGoals;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String overallSelfComment;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String overallManagerComment;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AppraisalCycle getAppraisalCycle() {
        return appraisalCycle;
    }

    public void setAppraisalCycle(AppraisalCycle appraisalCycle) {
        this.appraisalCycle = appraisalCycle;
    }

    public Integer getOverallSelfRating() {
        return overallSelfRating;
    }

    public void setOverallSelfRating(Integer overallSelfRating) {
        this.overallSelfRating = overallSelfRating;
    }

    public String getOverallSelfComment() {
        return overallSelfComment;
    }

    public void setOverallSelfComment(String overallSelfComment) {
        this.overallSelfComment = overallSelfComment;
    }

    public Integer getOverallManagerRating() {
        return overallManagerRating;
    }

    public void setOverallManagerRating(Integer overallManagerRating) {
        this.overallManagerRating = overallManagerRating;
    }

    public String getOverallManagerComment() {
        return overallManagerComment;
    }

    public void setOverallManagerComment(String overallManagerComment) {
        this.overallManagerComment = overallManagerComment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }

    public LocalDate getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(LocalDate reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public LocalDate getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(LocalDate finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    // ================= PROJECT & CONTRIBUTION =================

    public String getProjectSummary() {
        return projectSummary;
    }

    public void setProjectSummary(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    public String getTaskSummary() {
        return taskSummary;
    }

    public void setTaskSummary(String taskSummary) {
        this.taskSummary = taskSummary;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

// ================= SELF ASSESSMENT =================

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getLearning() {
        return learning;
    }

    public void setLearning(String learning) {
        this.learning = learning;
    }

    public String getChallenges() {
        return challenges;
    }

    public void setChallenges(String challenges) {
        this.challenges = challenges;
    }

    public String getFutureGoals() {
        return futureGoals;
    }

    public void setFutureGoals(String futureGoals) {
        this.futureGoals = futureGoals;
    }
}