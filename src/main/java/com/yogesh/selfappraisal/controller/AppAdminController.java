package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.entity.AppraisalCycle;
import com.yogesh.selfappraisal.entity.SelfAppraisal;
import com.yogesh.selfappraisal.repository.AppraisalCycleRepository;
import com.yogesh.selfappraisal.repository.SelfAppraisalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AppAdminController {

    @Autowired
    private AppraisalCycleRepository appraisalCycleRepository;

    @Autowired
    private SelfAppraisalRepository selfAppraisalRepository;

    @GetMapping("/appraisal/admin/cycles")
    public String listCycles(Model model) {

        List<AppraisalCycle> cycles =
                appraisalCycleRepository.findAll();

        model.addAttribute("cycles", cycles);
        model.addAttribute("content",
                "dashboard-pages/appraisal-cycles");

        return "dashboard";
    }

    @GetMapping("/appraisal/admin/cycle/create")
    public String createCycle(Model model) {

        model.addAttribute("cycle", new AppraisalCycle());
        model.addAttribute("content",
                "dashboard-pages/appraisal-cycle-form");

        return "dashboard";
    }

    @PostMapping("/appraisal/admin/cycle/save")
    public String saveCycle(AppraisalCycle cycle) {

        if(cycle.getStatus() == null)
            cycle.setStatus("DRAFT");

        appraisalCycleRepository.save(cycle);

        return "redirect:/appraisal/admin/cycles";
    }

    @GetMapping("/appraisal/admin/cycle/edit/{id}")
    public String editCycle(@PathVariable Long id,
                            Model model) {

        AppraisalCycle cycle =
                appraisalCycleRepository.findById(id)
                        .orElse(null);

        model.addAttribute("cycle", cycle);
        model.addAttribute("content",
                "dashboard-pages/appraisal-cycle-edit");

        return "dashboard";
    }

//    @PostMapping("/appraisal/admin/cycle/update")
//    public String updateCycle(AppraisalCycle cycle) {
//
//        if(cycle.getId() == null)
//            return "redirect:/appraisal/admin/cycles";
//
//        AppraisalCycle existing =
//                appraisalCycleRepository.findById(cycle.getId())
//                        .orElse(null);
//
//        if(existing != null){
//
//            existing.setTitle(cycle.getTitle());
//            existing.setStartDate(cycle.getStartDate());
//            existing.setEndDate(cycle.getEndDate());
//            existing.setStatus(cycle.getStatus());
//
//            appraisalCycleRepository.save(existing);
//        }
//
//        return "redirect:/appraisal/admin/cycles";
//    }

    @PostMapping("/appraisal/admin/cycle/update")
    public String updateCycle(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String status) {

        AppraisalCycle existing =
                appraisalCycleRepository.findById(id).orElse(null);

        if(existing != null){

            existing.setTitle(title);

            existing.setStartDate(LocalDate.parse(startDate));
            existing.setEndDate(LocalDate.parse(endDate));

            existing.setStatus(status);

            appraisalCycleRepository.save(existing);
        }

        return "redirect:/appraisal/admin/cycles";
    }

    @GetMapping("/appraisal/admin/finalize")
    public String finalizePage(Model model){

        List<SelfAppraisal> appraisals =
                selfAppraisalRepository.findByStatus("REVIEWED");

        model.addAttribute("appraisals", appraisals);

        model.addAttribute("content",
                "dashboard-pages/appraisal-admin-finalize.html");

        return "dashboard";
    }

    @PostMapping("/appraisal/admin/finalize/{id}")
    public String finalizeAppraisal(@PathVariable Long id){

        SelfAppraisal appraisal =
                selfAppraisalRepository.findById(id).orElseThrow();

        appraisal.setStatus("FINALIZED");
        appraisal.setFinalizedDate(LocalDate.now());

        selfAppraisalRepository.save(appraisal);

        return "redirect:/appraisal/admin/finalize";
    }

}