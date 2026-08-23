package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.Company;
import com.yogesh.selfappraisal.repository.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepo;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String company(Model model){

        model.addAttribute("companies", companyRepo.findAll());
        model.addAttribute("content", "dashboard-pages/company");

        return "dashboard";
    }

    // ===============================
    // VIEW COMPANY
    // ===============================
    @GetMapping("/view/{id}")
    public String viewCompany(@PathVariable Integer id, Model model){

        Company company = companyRepo.findById(id).orElse(null);

        if(company == null){
            return "redirect:/company";
        }

        model.addAttribute("company", company);
        model.addAttribute("content", "dashboard-pages/company-view");

        return "dashboard";
    }

    @GetMapping("/edit/{id}")
    public String editCompany(@PathVariable Integer id, Model model){

        Company company = companyRepo.findById(id).orElse(null);

        if(company == null){
            return "redirect:/company";
        }

        model.addAttribute("company", company);
        model.addAttribute("content", "dashboard-pages/company-edit");

        return "dashboard";
    }

    @PostMapping("/update")
    public String updateCompany(@ModelAttribute Company company){

        Company existing = companyRepo
                .findById(company.getCompanyId())
                .orElse(null);

        if(existing == null){
            return "redirect:/company";
        }

        // Update ALL editable fields
        existing.setCompanyName(company.getCompanyName());
        existing.setCompanyCode(company.getCompanyCode());
        existing.setRegistrationNo(company.getRegistrationNo());
        existing.setSector(company.getSector());

        existing.setGstRegistered(company.isGstRegistered());
        existing.setGstPercentage(company.getGstPercentage());
        existing.setGstRegistrationNo(company.getGstRegistrationNo());

        existing.setWorkingHours(company.getWorkingHours());
        existing.setOfficeStartTime(company.getOfficeStartTime());
        existing.setOfficeEndTime(company.getOfficeEndTime());
        existing.setPayrollCountry(company.getPayrollCountry());

        existing.setCompanyLoginId(company.getCompanyLoginId());

        companyRepo.save(existing);

        return "redirect:/company/view/" + existing.getCompanyId();
    }
}
