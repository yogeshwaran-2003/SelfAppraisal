package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Company;
import com.yogesh.selfappraisal.entity.PayrollTransaction;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.PayrollRepository;
import com.yogesh.selfappraisal.service.PayrollService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payroll/admin")
public class PayrollAdminController {

    private final PayrollService payrollService;

    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;

    public PayrollAdminController(PayrollService payrollService,
                                  EmployeeRepository employeeRepository,
                                  PayrollRepository payrollRepository) {

        this.payrollService = payrollService;
        this.employeeRepository = employeeRepository;
        this.payrollRepository = payrollRepository;
    }

    // ============================
    // ADMIN PAYROLL PAGE
    // ============================

    @GetMapping
    public String payrollAdminPage(Model model) {

        int month = java.time.LocalDate.now().getMonthValue();
        int year = java.time.LocalDate.now().getYear();

        model.addAttribute("records",
                payrollService.getPayrollByMonth(month, year));

        model.addAttribute("month", month);
        model.addAttribute("year", year);

        model.addAttribute("content",
                "dashboard-pages/payroll-admin.html");

        return "dashboard";
    }

    // ============================
    // GENERATE PAYROLL
    // ============================

    @PostMapping("/generate")
    public String generatePayroll(@RequestParam int month,
                                  @RequestParam int year) {

        payrollService.generatePayroll(month, year);

        return "redirect:/payroll/admin/view?month=" + month + "&year=" + year;
    }

    // ============================
    // VIEW PAYROLL
    // ============================

    @GetMapping("/view")
    public String viewPayroll(@RequestParam int month,
                              @RequestParam int year,
                              Model model) {

        model.addAttribute("records",
                payrollService.getPayrollByMonth(month, year));

        model.addAttribute("month", month);
        model.addAttribute("year", year);

        model.addAttribute("content",
                "dashboard-pages/payroll-admin.html");

        return "dashboard";
    }

    @GetMapping("/history")
    public String payrollHistory(
            @RequestParam(required=false) Long employeeId,
            @RequestParam(required=false) Integer month,
            @RequestParam(required=false) Integer year,
            Model model){

        List<PayrollTransaction> records =
                payrollService.getFilteredPayroll(employeeId, month, year);

        model.addAttribute("records", records);
        model.addAttribute("employees", employeeRepository.findAll());

        model.addAttribute("content",
                "dashboard-pages/payroll-admin-history.html");

        return "dashboard";
    }

//    @GetMapping("/payslip/{id}")
//    public String viewPayslip(@PathVariable Long id, Model model){
//
//        PayrollTransaction payroll =
//                payrollRepository.findById(id).orElseThrow();
//
//        Company company =
//                payroll.getEmployee().getCompany();
//
//        model.addAttribute("payroll", payroll);
//        model.addAttribute("company", company);
//
//        model.addAttribute("content",
//                "dashboard-pages/payslip-view.html");
//
//        return "dashboard";
//    }

    @GetMapping("/payslip/{id}")
    public String viewPayslip(@PathVariable Long id, Model model) {

        PayrollTransaction payroll =
                payrollRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Payroll not found"));

        Company company = payroll.getEmployee().getCompany();

        model.addAttribute("payroll", payroll);
        model.addAttribute("company", company);

        model.addAttribute("content",
                "dashboard-pages/payslip-view.html");

        return "dashboard";
    }

}