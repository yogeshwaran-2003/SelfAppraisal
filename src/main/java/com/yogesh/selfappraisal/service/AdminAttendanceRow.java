package com.yogesh.selfappraisal.service;

public class AdminAttendanceRow {

    private Long employeeId;
    private String employeeName;
    private int present;
    private int absent;
    private int halfDay;
    private int leave;
    private int holiday;
    private double payableDays;

    public AdminAttendanceRow(Long employeeId,
                              String employeeName,
                              int present,
                              int absent,
                              int halfDay,
                              int leave,
                              int holiday,
                              double payableDays) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.present = present;
        this.absent = absent;
        this.halfDay = halfDay;
        this.leave = leave;
        this.holiday = holiday;
        this.payableDays = payableDays;
    }

    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public int getPresent() { return present; }
    public int getAbsent() { return absent; }
    public int getHalfDay() { return halfDay; }
    public int getLeave() { return leave; }
    public int getHoliday() { return holiday; }
    public double getPayableDays() { return payableDays; }
}