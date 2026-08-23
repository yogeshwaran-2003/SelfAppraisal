package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Department;
import com.yogesh.selfappraisal.entity.Employee;
import com.yogesh.selfappraisal.entity.User;

import com.yogesh.selfappraisal.repository.CompanyRepository;
import com.yogesh.selfappraisal.repository.DepartmentRepository;
import com.yogesh.selfappraisal.repository.EmployeeRepository;
import com.yogesh.selfappraisal.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private UserRepository userRepo;

//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//    @GetMapping
//    public String employees(Model model, HttpSession session){
//        User user = (User) session.getAttribute("user");
//        model.addAttribute("employees", employeeRepo.findAll());
//        model.addAttribute("content", "dashboard-pages/employees");
//        return "dashboard";
//    }

//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//    @GetMapping
//    public String employees(Model model) {
//
//        Authentication auth =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        String username = auth.getName();
//
//        User user = userRepo.findByUsername(username);
//
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        // ADMIN → show all employees
//        if (user.getRole().getRoleName().equals("ADMIN")) {
//
//            model.addAttribute("employees", employeeRepo.findAll());
//
//        }
//        // MANAGER → show department employees only
//        else if (user.getRole().getRoleName().equals("MANAGER")) {
//
//            if(user.getEmployee() != null &&
//                    user.getEmployee().getDepartment() != null){
//
//                Integer deptId =
//                        user.getEmployee().getDepartment().getDepartmentId();
//
//                model.addAttribute("employees",
//                        employeeRepo.findByDepartmentDepartmentId(deptId));
//            }
//        }
//
//        model.addAttribute("content", "dashboard-pages/employees");
//
//        return "dashboard";
//    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public String employees(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        // ADMIN → show all employees
        if (user.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {

            model.addAttribute("employees",
                    employeeRepo.findAll());

        }

        // MANAGER → show only department employees
        else if (user.getRole().getRoleName().equalsIgnoreCase("MANAGER")) {

            if(user.getEmployee() != null &&
                    user.getEmployee().getDepartment() != null){

                Integer deptId =
                        user.getEmployee().getDepartment().getDepartmentId();

                model.addAttribute("employees",
                        employeeRepo.findByDepartmentDepartmentId(deptId));

            } else {

                model.addAttribute("employees",
                        employeeRepo.findAll()); // fallback
            }
        }

        model.addAttribute("content", "dashboard-pages/employees");

        return "dashboard";
    }

    @GetMapping("/create")
    public String createEmployee(Model model){

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userRepo.findByUsername(username);

        if(user == null){
            return "redirect:/login";
        }

        model.addAttribute("employee", new Employee());

        model.addAttribute("departments",
                departmentRepo.findByCompanyCompanyId(
                        user.getCompany().getCompanyId()
                ));

        model.addAttribute("employees",
                employeeRepo.findByIsReportingManagerTrue());

        model.addAttribute("content", "dashboard-pages/employee-form");

        return "dashboard";
    }

    @PostMapping("/save")
    public String save(Employee employee) {

        // 🔐 Get logged-in user from Spring Security
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        // Set company from logged user
        employee.setCompany(user.getCompany());

        // Handle checkboxes safely
        if (employee.getIsActive() == null)
            employee.setIsActive(false);

        if (employee.getIsReportingManager() == null)
            employee.setIsReportingManager(false);

        employeeRepo.save(employee);

        return "redirect:/employees";
    }

    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model){

        Employee employee = employeeRepo.findById(id).orElse(null);

        model.addAttribute("employee", employee);
        model.addAttribute("content", "dashboard-pages/employee-view");

        return "dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id,
                               Model model) {

        // 🔐 Get logged-in user from Spring Security
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        Employee employee = employeeRepo.findById(id).orElse(null);

        model.addAttribute("employee", employee);

        model.addAttribute("departments",
                departmentRepo.findByCompanyCompanyId(
                        user.getCompany().getCompanyId()
                ));

        // Keep your existing repository method
        model.addAttribute("employees",
                employeeRepo.findByIsReportingManagerTrue());

        model.addAttribute("content", "dashboard-pages/employee-edit");

        return "dashboard";
    }

    @PostMapping("/update")
    public String updateEmployee(Employee employee,
                                 @RequestParam(required = false) Long reportingToId) {

        // 🔐 Get logged-in user from Spring Security
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        User user = userRepo.findByUsername(username);

        if (user == null)
            return "redirect:/login";


        // Fetch existing employee
        Employee existing = employeeRepo
                .findById(employee.getEmployeeId())
                .orElse(null);

        if (existing == null)
            return "redirect:/employees";


        // ======================
        // DEPARTMENT
        // ======================

        if (employee.getDepartment() != null &&
                employee.getDepartment().getDepartmentId() != null) {

            Department dept = departmentRepo
                    .findById(employee.getDepartment().getDepartmentId())
                    .orElse(null);

            existing.setDepartment(dept);
        } else {
            existing.setDepartment(null);
        }


        // ======================
        // REPORTING MANAGER
        // ======================

        if (reportingToId != null) {
            Employee manager = employeeRepo
                    .findById(reportingToId)
                    .orElse(null);

            existing.setReportingTo(manager);
        } else {
            existing.setReportingTo(null);
        }


        // ======================
        // PERSONAL INFO
        // ======================

        existing.setEmployeeCode(employee.getEmployeeCode());
        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setGender(employee.getGender());
        existing.setDob(employee.getDob());
        existing.setMobileNo(employee.getMobileNo());
        existing.setEmail(employee.getEmail());
        existing.setNationality(employee.getNationality());
        existing.setIdType(employee.getIdType());
        existing.setIdNumber(employee.getIdNumber());
        existing.setMaritalStatus(employee.getMaritalStatus());


        // ======================
        // WORK DETAILS
        // ======================

        existing.setDesignation(employee.getDesignation());
        existing.setWorkingType(employee.getWorkingType());
        existing.setJoiningDate(employee.getJoiningDate());
        existing.setLocation(employee.getLocation());
        existing.setWorkingHours(employee.getWorkingHours());
        existing.setJobDescription(employee.getJobDescription());

        existing.setIsReportingManager(
                employee.getIsReportingManager() != null
                        ? employee.getIsReportingManager()
                        : false
        );


        // ======================
        // ATTENDANCE
        // ======================

        existing.setShift(employee.getShift());
        existing.setOfficeStartTime(employee.getOfficeStartTime());
        existing.setOfficeEndTime(employee.getOfficeEndTime());
        existing.setAttendanceRequired(employee.getAttendanceRequired());
        existing.setAttendanceRequiredForPay(employee.getAttendanceRequiredForPay());


        // ======================
        // STATUS
        // ======================

        existing.setProbation(employee.getProbation());
        existing.setResignation(employee.getResignation());
        existing.setResignationType(employee.getResignationType());
        existing.setNoticePeriodDays(employee.getNoticePeriodDays());
        existing.setCessationDate(employee.getCessationDate());
        existing.setResignationRemarks(employee.getResignationRemarks());


        // ======================
        // ADDRESS
        // ======================

        existing.setAddress(employee.getAddress());
        existing.setCity(employee.getCity());
        existing.setCountry(employee.getCountry());
        existing.setPostalCode(employee.getPostalCode());


        // ======================
        // ACTIVE
        // ======================

        existing.setIsActive(employee.getIsActive());


        employeeRepo.save(existing);

        return "redirect:/employees";
    }

    @GetMapping("/toggle/{id}")
    public String toggleEmployee(@PathVariable Long id){

        Employee employee = employeeRepo.findById(id).orElse(null);

        if(employee != null){

            // toggle active status safely
            Boolean currentStatus = employee.getIsActive();

            if(currentStatus == null){
                employee.setIsActive(true);
            }else{
                employee.setIsActive(!currentStatus);
            }

            employeeRepo.save(employee);
        }
        return "redirect:/employees";
    }
}