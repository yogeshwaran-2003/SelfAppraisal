package com.yogesh.selfappraisal.service;

import com.yogesh.selfappraisal.entity.*;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.PayrollRepository;
import com.yogesh.selfappraisal.repository.SelfAppraisalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;
    private final SelfAppraisalRepository selfAppraisalRepository;

    public PayrollService(PayrollRepository payrollRepository,
                          EmployeeRepository employeeRepository,
                          AttendanceService attendanceService,
                          SelfAppraisalRepository selfAppraisalRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceService = attendanceService;
        this.selfAppraisalRepository = selfAppraisalRepository;
    }

    // ============================
    // GENERATE PAYROLL FOR MONTH
    // ============================

    @Transactional
    public void generatePayroll(int month, int year) {

        List<Employee> employees = employeeRepository.findByIsActiveTrue();

        for (Employee emp : employees) {

            Optional<PayrollTransaction> existing =
                    payrollRepository.findByEmployeeAndMonthAndYear(emp, month, year);

            if (existing.isPresent()) {
                continue; // payroll already generated
            }

            PayrollTransaction payroll =
                    calculateEmployeePayroll(emp, month, year);

            payrollRepository.save(payroll);
        }
    }

    // ============================
    // CALCULATE EMPLOYEE PAYROLL
    // ============================

    private PayrollTransaction calculateEmployeePayroll(Employee emp,
                                                        int month,
                                                        int year) {

        AttendanceSummary summary =
                attendanceService.getMonthlySummary(emp, month, year);

        double baseSalary = emp.getBasicSalary();

        int present = summary.getPresent();
        int absent = summary.getAbsent();
        int halfDay = summary.getHalfDay();
        int leave = summary.getLeave();
        int holiday = summary.getHoliday();

        // ============================
        // DAILY SALARY
        // ============================

        int totalDays = YearMonth.of(year, month).lengthOfMonth();

        double dailySalary = baseSalary / totalDays;

        // ============================
        // ATTENDANCE DEDUCTION
        // ============================

        double deduction =
                (absent * dailySalary) +
                        (halfDay * (dailySalary / 2));

        // ============================
        // BONUS FROM APPRAISAL
        // ============================

        double bonus = 0;
        SelfAppraisal appraisal = null;

        Optional<SelfAppraisal> appraisalOpt =
                selfAppraisalRepository
                        .findTopByEmployeeAndStatusOrderByFinalizedDateDesc(
                                emp,
                                "FINALIZED"
                        );

        if (appraisalOpt.isPresent()) {

            appraisal = appraisalOpt.get();

            int rating = appraisal.getOverallManagerRating();

            if (rating >= 5) {
                bonus = baseSalary * 0.20;
            }
            else if (rating == 4) {
                bonus = baseSalary * 0.10;
            }
            else if (rating == 3) {
                bonus = baseSalary * 0.05;
            }
        }

        // ============================
        // FINAL SALARY
        // ============================

        double grossSalary = baseSalary + bonus;
        double netSalary = grossSalary - deduction;

        // ============================
        // CREATE PAYROLL
        // ============================

        PayrollTransaction payroll = new PayrollTransaction();

        payroll.setEmployee(emp);
        payroll.setMonth(month);
        payroll.setYear(year);

        payroll.setPresentDays(present);
        payroll.setAbsentDays(absent);
        payroll.setHalfDays(halfDay);
        payroll.setLeaveDays(leave);
        payroll.setHolidays(holiday);

        payroll.setBaseSalary(baseSalary);
        payroll.setBonus(bonus);
        payroll.setDeduction(deduction);
        payroll.setGrossSalary(grossSalary);
        payroll.setNetSalary(netSalary);

        payroll.setAppraisal(appraisal);
        payroll.setStatus("PROCESSED");

        return payroll;
    }

    // ============================
    // ADMIN VIEW
    // ============================

    public List<PayrollTransaction> getPayrollByMonth(int month, int year) {

        return payrollRepository.findByMonthAndYear(month, year);
    }

    // ============================
    // EMPLOYEE PAYSLIP
    // ============================

    public List<PayrollTransaction> getEmployeePayroll(Employee emp) {

        return payrollRepository
                .findByEmployeeOrderByYearDescMonthDesc(emp);
    }

    public List<PayrollTransaction> getAllPayroll(){
        return payrollRepository.findAll();
    }

    public List<PayrollTransaction> getFilteredPayroll(
            Long employeeId,
            Integer month,
            Integer year){

        if(employeeId != null)
            return payrollRepository.findByEmployeeEmployeeId(employeeId);

        if(month != null && year != null)
            return payrollRepository.findByMonthAndYear(month, year);

        return payrollRepository.findAll();
    }
}