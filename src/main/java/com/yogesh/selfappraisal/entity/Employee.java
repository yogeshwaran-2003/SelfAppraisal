package com.yogesh.selfappraisal.entity;

import com.yogesh.selfappraisal.entity.Company;
import com.yogesh.selfappraisal.entity.Department;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

    // ========================
    // PRIMARY KEY
    // ========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;


    // ========================
    // PERSONAL INFORMATION
    // ========================
    @Column(nullable = false, unique = true, length = 50)
    private String employeeCode;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dob;

    private String mobileNo;

    private String email;

    private String nationality;

    private String idType;

    private String idNumber;

    private String maritalStatus;


    // ========================
    // WORK DETAILS
    // ========================

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private String designation;

    private String workingType; // Full Time / Contract

    private LocalDate joiningDate;

    private String location;

    private String workingHours;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 2000)
    private String jobDescription;

    @Column(nullable = false)
    private Double basicSalary;


    // Self reference (Reporting Manager)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_to")
    private Employee reportingTo;

    @Column(name = "is_reporting_manager")
    private Boolean isReportingManager;

    // ========================
    // ATTENDANCE DETAILS
    // ========================

    private String shift;

    private LocalTime officeStartTime;

    private LocalTime officeEndTime;

    private Boolean attendanceRequired;

    private Boolean attendanceRequiredForPay;


    // ========================
    // STATUS / RESIGNATION
    // ========================

    private Boolean probation;

    private Boolean resignation;

    private String resignationType;

    private Integer noticePeriodDays;

    private LocalDate cessationDate;

    @Column(length = 2000)
    private String resignationRemarks;


    // ========================
    // ADDRESS
    // ========================

    private String address;

    private String city;

    private String country;

    private String postalCode;


    // ========================
    // EMERGENCY CONTACT
    // ========================

    private String emergencyContactName;

    private String emergencyContactMobile;

    private String relationship;


    // ========================
    // SYSTEM LINKS
    // ========================

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    // PRIMARY KEY
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }


    // PERSONAL INFORMATION
    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }


    // WORK DETAILS
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorkingType() {
        return workingType;
    }

    public void setWorkingType(String workingType) {
        this.workingType = workingType;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    // REPORTING TO
    public Employee getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(Employee reportingTo) {
        this.reportingTo = reportingTo;
    }

    public Boolean getIsReportingManager() {
        return isReportingManager;
    }

    public void setIsReportingManager(Boolean isReportingManager) {
        this.isReportingManager = isReportingManager;
    }


    // ATTENDANCE
    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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

    public Boolean getAttendanceRequired() {
        return attendanceRequired;
    }

    public void setAttendanceRequired(Boolean attendanceRequired) {
        this.attendanceRequired = attendanceRequired;
    }

    public Boolean getAttendanceRequiredForPay() {
        return attendanceRequiredForPay;
    }

    public void setAttendanceRequiredForPay(Boolean attendanceRequiredForPay) {
        this.attendanceRequiredForPay = attendanceRequiredForPay;
    }


    // STATUS
    public Boolean getProbation() {
        return probation;
    }

    public void setProbation(Boolean probation) {
        this.probation = probation;
    }

    public Boolean getResignation() {
        return resignation;
    }

    public void setResignation(Boolean resignation) {
        this.resignation = resignation;
    }

    public String getResignationType() {
        return resignationType;
    }

    public void setResignationType(String resignationType) {
        this.resignationType = resignationType;
    }

    public Integer getNoticePeriodDays() {
        return noticePeriodDays;
    }

    public void setNoticePeriodDays(Integer noticePeriodDays) {
        this.noticePeriodDays = noticePeriodDays;
    }

    public LocalDate getCessationDate() {
        return cessationDate;
    }

    public void setCessationDate(LocalDate cessationDate) {
        this.cessationDate = cessationDate;
    }

    public String getResignationRemarks() {
        return resignationRemarks;
    }

    public void setResignationRemarks(String resignationRemarks) {
        this.resignationRemarks = resignationRemarks;
    }


    // ADDRESS
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    // EMERGENCY
    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactMobile() {
        return emergencyContactMobile;
    }

    public void setEmergencyContactMobile(String emergencyContactMobile) {
        this.emergencyContactMobile = emergencyContactMobile;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }


    // COMPANY
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}