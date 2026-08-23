//package com.yogesh.selfappraisal.service;
//
//import com.yogesh.selfappraisal.entity.*;
//import com.yogesh.selfappraisal.repository.AttendanceRepository;
//import com.yogesh.selfappraisal.repository.EmployeeRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.*;
//
//@Service
//public class AttendanceService {
//
//    private final AttendanceRepository attendanceRepository;
//    private final EmployeeRepository employeeRepository;
//
//    public AttendanceService(AttendanceRepository attendanceRepository,
//                             EmployeeRepository employeeRepository) {
//        this.attendanceRepository = attendanceRepository;
//        this.employeeRepository = employeeRepository;
//    }
//
//    public Map<Long, AttendanceStatus> getAttendanceForDate(List<Employee> employees,
//                                                            LocalDate date) {
//
//        Map<Long, AttendanceStatus> map = new HashMap<>();
//
//        for (Employee emp : employees) {
//
//            attendanceRepository.findByEmployeeAndDate(emp, date)
//                    .ifPresent(att -> map.put(emp.getEmployeeId(), att.getStatus()));
//        }
//
//        return map;
//    }
//
//    // ============================
//    // LOAD MANAGER TEAM
//    // ============================
//    public List<Employee> getManagerTeam(Employee manager) {
//        return employeeRepository.findByReportingToAndIsActiveTrue(manager);
//    }
//
//    // ============================
//    // SAVE / UPDATE ATTENDANCE
//    // ============================
//    @Transactional
//    public void saveAttendance(LocalDate date,
//                               Map<Long, AttendanceStatus> attendanceMap,
//                               User managerUser) {
//
//        for (Map.Entry<Long, AttendanceStatus> entry : attendanceMap.entrySet()) {
//
//            Long empId = entry.getKey();
//            AttendanceStatus status = entry.getValue();
//
//            Optional<Employee> empOpt = employeeRepository.findById(empId);
//            if (empOpt.isEmpty()) continue;
//
//            Employee employee = empOpt.get();
//
//            Optional<Attendance> existing =
//                    attendanceRepository.findByEmployeeAndDate(employee, date);
//
//            if (existing.isPresent()) {
//
//                Attendance attendance = existing.get();
//                attendance.setStatus(status);
//                attendance.setMarkedBy(managerUser);
//                attendanceRepository.save(attendance);
//
//            } else {
//
//                Attendance attendance = new Attendance();
//                attendance.setEmployee(employee);
//                attendance.setDate(date);
//                attendance.setStatus(status);
//                attendance.setMarkedBy(managerUser);
//                attendance.setMonth(date.getMonthValue());
//                attendance.setYear(date.getYear());
//
//                attendanceRepository.save(attendance);
//            }
//        }
//    }
//
//    public AttendanceSummary getMonthlySummary(Employee employee,
//                                               int month,
//                                               int year) {
//
//        List<Attendance> records =
//                attendanceRepository.findByEmployeeAndMonthAndYear(employee, month, year);
//
//        int present = 0;
//        int absent = 0;
//        int halfDay = 0;
//        int leave = 0;
//
//        for (Attendance att : records) {
//
//            switch (att.getStatus()) {
//                case PRESENT -> present++;
//                case ABSENT -> absent++;
//                case HALF_DAY -> halfDay++;
//                case LEAVE -> leave++;
//            }
//        }
//
//        double payableDays = present + leave + (halfDay * 0.5);
//
//        return new AttendanceSummary(
//                present, absent, halfDay, leave, payableDays
//        );
//    }
//
//    public List<Attendance> getMonthlyRecords(Employee employee,
//                                              int month,
//                                              int year) {
//
//        return attendanceRepository
//                .findByEmployeeAndMonthAndYear(employee, month, year);
//    }
//}

package com.yogesh.selfappraisal.service;

import com.yogesh.selfappraisal.entity.*;
import com.yogesh.selfappraisal.repository.AttendanceRepository;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // ======================================================
    // GET ATTENDANCE FOR SPECIFIC DATE (Manager Screen)
    // ======================================================
    public Map<Long, AttendanceStatus> getAttendanceForDate(List<Employee> employees,
                                                            LocalDate date) {

        Map<Long, AttendanceStatus> map = new HashMap<>();

        for (Employee emp : employees) {
            attendanceRepository.findByEmployeeAndDate(emp, date)
                    .ifPresent(att -> map.put(emp.getEmployeeId(), att.getStatus()));
        }

        return map;
    }

    // ======================================================
    // LOAD MANAGER TEAM
    // ======================================================
    public List<Employee> getManagerTeam(Employee manager) {
        return employeeRepository.findByReportingToAndIsActiveTrue(manager);
    }

    // ======================================================
    // SAVE / UPDATE ATTENDANCE (Upsert Logic)
    // ======================================================
    @Transactional
    public void saveAttendance(LocalDate date,
                               Map<Long, AttendanceStatus> attendanceMap,
                               User managerUser) {

        // Optional: Block future dates
        if (date.isAfter(LocalDate.now())) {
            throw new RuntimeException("Future attendance marking not allowed.");
        }

        for (Map.Entry<Long, AttendanceStatus> entry : attendanceMap.entrySet()) {

            Long empId = entry.getKey();
            AttendanceStatus status = entry.getValue();

            Optional<Employee> empOpt = employeeRepository.findById(empId);
            if (empOpt.isEmpty()) continue;

            Employee employee = empOpt.get();

            Optional<Attendance> existing =
                    attendanceRepository.findByEmployeeAndDate(employee, date);

            if (existing.isPresent()) {

                Attendance attendance = existing.get();
                attendance.setStatus(status);
                attendance.setMarkedBy(managerUser);

                attendanceRepository.save(attendance);

            } else {

                Attendance attendance = new Attendance();
                attendance.setEmployee(employee);
                attendance.setDate(date);
                attendance.setStatus(status);
                attendance.setMarkedBy(managerUser);
                attendance.setMonth(date.getMonthValue());
                attendance.setYear(date.getYear());

                attendanceRepository.save(attendance);
            }
        }
    }

    // ======================================================
    // MONTHLY SUMMARY (Payroll Ready)
    // HOLIDAY is ignored in salary calculation
    // ======================================================
    public AttendanceSummary getMonthlySummary(Employee employee,
                                               int month,
                                               int year) {

        List<Attendance> records =
                attendanceRepository.findByEmployeeAndMonthAndYear(employee, month, year);

        int present = 0;
        int absent = 0;
        int halfDay = 0;
        int leave = 0;
        int holiday = 0;

        for (Attendance att : records) {

            switch (att.getStatus()) {
                case PRESENT -> present++;
                case ABSENT -> absent++;
                case HALF_DAY -> halfDay++;
                case LEAVE -> leave++;
                case HOLIDAY -> holiday++;   // counted for display only
            }
        }

        // IMPORTANT:
        // HOLIDAY is NOT included in payable days
        double payableDays = present + leave + (halfDay * 0.5);

        return new AttendanceSummary(
                present,
                absent,
                halfDay,
                leave,
                holiday,
                payableDays
        );
    }

    // ======================================================
    // MONTHLY RECORDS (Date-wise Table)
    // ======================================================
    public List<Attendance> getMonthlyRecords(Employee employee,
                                              int month,
                                              int year) {

        return attendanceRepository
                .findByEmployeeAndMonthAndYear(employee, month, year);
    }

    public List<AdminAttendanceRow> getMonthlyAdminReport(int month, int year) {

        List<Employee> employees = employeeRepository.findByIsActiveTrue();

        List<AdminAttendanceRow> report = new ArrayList<>();

        for (Employee emp : employees) {

            AttendanceSummary summary =
                    getMonthlySummary(emp, month, year);

            report.add(new AdminAttendanceRow(
                    emp.getEmployeeId(),
                    emp.getFirstName() + " " + emp.getLastName(),
                    summary.getPresent(),
                    summary.getAbsent(),
                    summary.getHalfDay(),
                    summary.getLeave(),
                    summary.getHoliday(),
                    summary.getPayableDays()
            ));
        }

        return report;
    }
}

