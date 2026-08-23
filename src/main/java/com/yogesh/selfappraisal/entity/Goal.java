package com.yogesh.selfappraisal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class Goal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private Integer weightage; // Percentage (e.g., 40%)

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "appraisal_cycle_id")
    private AppraisalCycle appraisalCycle;

    private String status; // ACTIVE / LOCKED

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeightage() {
        return weightage;
    }

    public void setWeightage(Integer weightage) {
        this.weightage = weightage;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}