package com.yogesh.selfappraisal.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "payroll_transactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id","month","year"})
        }
)
public class PayrollTransaction extends BaseEntity {

    // ========================
    // PRIMARY KEY
    // ========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========================
    // EMPLOYEE
    // ========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // ========================
    // PAYROLL PERIOD
    // ========================
    private Integer month;

    private Integer year;

    // ========================
    // ATTENDANCE DATA
    // ========================
    private Integer presentDays;

    private Integer absentDays;

    private Integer halfDays;

    private Integer leaveDays;

    private Integer holidays;

    // ========================
    // SALARY DATA
    // ========================
    private Double baseSalary;

    private Double bonus;

    private Double deduction;

    private Double grossSalary;

    private Double netSalary;

    // ========================
    // APPRAISAL LINK
    // ========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appraisal_id")
    private SelfAppraisal appraisal;

    // ========================
    // STATUS
    // ========================
    private String status; // DRAFT / PROCESSED / PAID


    // ========================
    // GETTERS & SETTERS
    // ========================

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Integer presentDays) {
        this.presentDays = presentDays;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getHalfDays() {
        return halfDays;
    }

    public void setHalfDays(Integer halfDays) {
        this.halfDays = halfDays;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Integer getHolidays() {
        return holidays;
    }

    public void setHolidays(Integer holidays) {
        this.holidays = holidays;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getDeduction() {
        return deduction;
    }

    public void setDeduction(Double deduction) {
        this.deduction = deduction;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public SelfAppraisal getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(SelfAppraisal appraisal) {
        this.appraisal = appraisal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}