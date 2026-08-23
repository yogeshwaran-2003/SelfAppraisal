//package com.yogesh.selfappraisal.controller;
//
//import com.yogesh.selfappraisal.entity.User;
//import com.yogesh.selfappraisal.service.PayrollService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class PayrollManagerController {
//
//    private final PayrollService payrollService;
//
//    public PayrollManagerController(PayrollService payrollService) {
//        this.payrollService = payrollService;
//    }
//
//    @GetMapping("/payroll/manager")
//    public String managerPayroll(Model model) {
//
//        model.addAttribute("content",
//                "dashboard-pages/payroll-manager.html");
//
//        return "dashboard";
//    }
//}

package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.User;
import com.yogesh.selfappraisal.repository.PayrollRepository;
import com.yogesh.selfappraisal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayrollManagerController {

    private final PayrollRepository payrollRepository;

//    public PayrollManagerController(PayrollRepository payrollRepository) {
//        this.payrollRepository = payrollRepository;
//    }
    private final UserRepository userRepository;

    public PayrollManagerController(PayrollRepository payrollRepository,
                                    UserRepository userRepository) {
        this.payrollRepository = payrollRepository;
        this.userRepository = userRepository;
    }

//    @GetMapping("/payroll/manager")
//    public String managerPayroll(Model model, Authentication auth) {
//
//        User user = (User) auth.getPrincipal();
//
//        Long managerId = user.getEmployee().getEmployeeId();
//
//        var records =
//                payrollRepository.findByEmployeeReportingToEmployeeId(managerId);
//
//        model.addAttribute("records", records);
//
//        model.addAttribute("content",
//                "dashboard-pages/payroll-manager.html");
//
//        return "dashboard";
//    }

    @GetMapping("/payroll/manager")
    public String managerPayroll(Model model, Authentication auth) {

        String username = auth.getName();   // logged-in username

        User user = userRepository.findByUsername(username);

        Long managerId = user.getEmployee().getEmployeeId();

        var records =
                payrollRepository.findByEmployeeReportingToEmployeeId(managerId);

        model.addAttribute("records", records);

        model.addAttribute("content",
                "dashboard-pages/payroll-manager.html");

        return "dashboard";
    }
}