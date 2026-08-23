package com.yogesh.selfappraisal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "goal_review")
public class GoalReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    // ===== Employee Side =====
    private Integer selfScore;

    @Column(length = 1000)
    private String selfComment;

    // ===== Manager Side =====
    private Integer managerScore;

    @Column(length = 1000)
    private String managerComment;

    @Column(length = 500)
    private String evidenceFile;
    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Integer getSelfScore() {
        return selfScore;
    }

    public void setSelfScore(Integer selfScore) {
        this.selfScore = selfScore;
    }

    public String getSelfComment() {
        return selfComment;
    }

    public void setSelfComment(String selfComment) {
        this.selfComment = selfComment;
    }

    public Integer getManagerScore() {
        return managerScore;
    }

    public void setManagerScore(Integer managerScore) {
        this.managerScore = managerScore;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getEvidenceFile() {
        return evidenceFile;
    }

    public void setEvidenceFile(String evidenceFile) {
        this.evidenceFile = evidenceFile;
    }
}