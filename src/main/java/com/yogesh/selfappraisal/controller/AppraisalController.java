package com.yogesh.selfappraisal.controller;

import com.yogesh.selfappraisal.repository.AppraisalCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppraisalController {

//    @GetMapping("/appraisal")
//    public String appraisalLanding(Model model) {
//
//        model.addAttribute("content", "dashboard-pages/appraisal");
//
//        return "dashboard";
//    }

    @Autowired
    private AppraisalCycleRepository appraisalCycleRepository;

    @GetMapping("/appraisal")
    public String appraisalLanding(Model model){

        boolean cycleOpen =
                appraisalCycleRepository.existsByStatus("OPEN");

        model.addAttribute("cycleOpen", cycleOpen);

        model.addAttribute("content","dashboard-pages/appraisal");

        return "dashboard";
    }
}