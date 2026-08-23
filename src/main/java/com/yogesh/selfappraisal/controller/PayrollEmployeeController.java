package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Company;
import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.PayrollTransaction;
import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.CompanyRepository;
import com.yogesh.selfappraisal.repository.PayrollRepository;
import com.yogesh.selfappraisal.repository.UserRepository;
import com.yogesh.selfappraisal.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PayrollEmployeeController {

    private final PayrollService payrollService;
    private final UserRepository userRepository;
    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private CompanyRepository companyRepository;
    public PayrollEmployeeController(PayrollService payrollService,
                                     UserRepository userRepository) {
        this.payrollService = payrollService;
        this.userRepository = userRepository;
    }

    @GetMapping("/payroll/employee")
    public String employeePayroll(Authentication auth,
                                  Model model) {

        User user = userRepository
                .findByUsername(auth.getName());

        Employee emp = user.getEmployee();

        model.addAttribute("records",
                payrollService.getEmployeePayroll(emp));

        model.addAttribute("content",
                "dashboard-pages/payroll-employee.html");

        return "dashboard";
    }

    @GetMapping("/payroll/employee/payslip/{id}")
    public String viewEmployeePayslip(@PathVariable Long id, Model model) {

        PayrollTransaction payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        Company company = companyRepository.findFirstByOrderByCompanyIdAsc();

        model.addAttribute("payroll", payroll);
        model.addAttribute("company", company);
        model.addAttribute("content",
                "dashboard-pages/payslip-view.html");

        return "dashboard";
    }
}