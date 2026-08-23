package com.yogesh.selfappraisal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;


    // ============================
    // AUTO SET BEFORE INSERT
    // ============================
    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        this.createdDate = now;
        this.updatedDate = now;

        // Default user ID (you can later replace with session user ID)
        if (this.createdBy == null) {
            this.createdBy = 1L;
        }

        if (this.updatedBy == null) {
            this.updatedBy = 1L;
        }
    }


    // ============================
    // AUTO SET BEFORE UPDATE
    // ============================
    @PreUpdate
    protected void onUpdate() {

        this.updatedDate = LocalDateTime.now();

        if (this.updatedBy == null) {
            this.updatedBy = 1L;
        }
    }


    // ============================
    // GETTERS AND SETTERS
    // ============================

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}