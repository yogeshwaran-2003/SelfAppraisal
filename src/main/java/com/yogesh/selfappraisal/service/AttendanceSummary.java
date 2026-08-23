//package com.yogesh.selfappraisal.service;
//
//public class AttendanceSummary {
//
//    private int present;
//    private int absent;
//    private int halfDay;
//    private int leave;
//    private double payableDays;
//
//    public AttendanceSummary(int present, int absent,
//                             int halfDay, int leave,
//                             double payableDays) {
//        this.present = present;
//        this.absent = absent;
//        this.halfDay = halfDay;
//        this.leave = leave;
//        this.payableDays = payableDays;
//    }
//
//    public int getPresent() { return present; }
//    public int getAbsent() { return absent; }
//    public int getHalfDay() { return halfDay; }
//    public int getLeave() { return leave; }
//    public double getPayableDays() { return payableDays; }
//}

package com.yogesh.selfappraisal.service;

public class AttendanceSummary {

    private int present;
    private int absent;
    private int halfDay;
    private int leave;
    private int holiday;
    private double payableDays;

    public AttendanceSummary(int present,
                             int absent,
                             int halfDay,
                             int leave,
                             int holiday,
                             double payableDays) {
        this.present = present;
        this.absent = absent;
        this.halfDay = halfDay;
        this.leave = leave;
        this.holiday = holiday;
        this.payableDays = payableDays;
    }

    public int getPresent() {
        return present;
    }

    public int getAbsent() {
        return absent;
    }

    public int getHalfDay() {
        return halfDay;
    }

    public int getLeave() {
        return leave;
    }

    public int getHoliday() {
        return holiday;
    }

    public double getPayableDays() {
        return payableDays;
    }
}