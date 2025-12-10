package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.PlanService;


import com.Eonline.Education.modals.Plan;
import com.Eonline.Education.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PlanService planService;


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/plans/{id}/price")
    public ResponseEntity<Plan> updatePlanPrice(@PathVariable Long id, @RequestParam int newPrice) {
        Plan plan = planService.getPlanById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        plan.setPrice(newPrice);
        planService.updatePlan(id, plan);
        return ResponseEntity.ok(plan);
    }

}
