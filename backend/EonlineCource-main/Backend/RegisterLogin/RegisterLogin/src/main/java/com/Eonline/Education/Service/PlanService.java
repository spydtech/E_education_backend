package com.Eonline.Education.Service;

import com.Eonline.Education.modals.Plan;
import com.Eonline.Education.repository.PlanRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    //@PostConstruct
    public void initPlans() {
        if (planRepository.count() == 0) {
            Plan basicPlan = new Plan(Plan.PlanType.BASIC, "Basic Plan Description");
            Plan premiumPlan = new Plan(Plan.PlanType.PREMIUM, "Premium Plan Description");
            Plan advancedPlan = new Plan(Plan.PlanType.ADVANCED, "Advanced Plan Description");
            planRepository.saveAll(Arrays.asList(basicPlan, premiumPlan, advancedPlan));
        }
    }
    public Optional<Plan> getPlanById(Long id) {
        return planRepository.findById(id);
    }
    public Plan updatePlan(Long id, Plan planDetails) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        plan.setName(planDetails.getName());
        plan.setDescription(planDetails.getDescription());
        plan.setPlanType(planDetails.getPlanType());
        return planRepository.save(plan);
    }

}
