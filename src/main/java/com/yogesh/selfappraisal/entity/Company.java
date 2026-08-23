package com.yogesh.selfappraisal.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name="company")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;

    private String companyName;
    private String companyCode;
    private String registrationNo;
    private String sector;

    private boolean isGstRegistered;
    private double gstPercentage;
    private String gstRegistrationNo;

    private double workingHours;
    private LocalTime officeStartTime;
    private LocalTime officeEndTime;
    private String payrollCountry;

    private String companyLoginId;
    private String logoPath;


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public boolean isGstRegistered() {
        return isGstRegistered;
    }

    public void setGstRegistered(boolean gstRegistered) {
        isGstRegistered = gstRegistered;
    }

    public double getGstPercentage() {
        return gstPercentage;
    }

    public void setGstPercentage(double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public String getGstRegistrationNo() {
        return gstRegistrationNo;
    }

    public void setGstRegistrationNo(String gstRegistrationNo) {
        this.gstRegistrationNo = gstRegistrationNo;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }

    public LocalTime getOfficeStartTime() {
        return officeStartTime;
    }

    public void setOfficeStartTime(LocalTime officeStartTime) {
        this.officeStartTime = officeStartTime;
    }

    public LocalTime getOfficeEndTime() {
        return officeEndTime;
    }

    public void setOfficeEndTime(LocalTime officeEndTime) {
        this.officeEndTime = officeEndTime;
    }

    public String getPayrollCountry() {
        return payrollCountry;
    }

    public void setPayrollCountry(String payrollCountry) {
        this.payrollCountry = payrollCountry;
    }

    public String getCompanyLoginId() {
        return companyLoginId;
    }

    public void setCompanyLoginId(String companyLoginId) {
        this.companyLoginId = companyLoginId;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
