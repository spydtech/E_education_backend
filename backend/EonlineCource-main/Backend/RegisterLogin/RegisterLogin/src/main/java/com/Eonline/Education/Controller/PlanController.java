package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.PlanService;
import com.Eonline.Education.Service.UserService;
import com.Eonline.Education.modals.Plan;

import com.Eonline.Education.modals.User;
import com.Eonline.Education.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    UserService userService;
    @Autowired
    PlanService planService;

    // Get all plans
    @GetMapping
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    // Get a plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        return plan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new plan - Admin only
    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        if (plan.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        Plan savedPlan = planRepository.save(plan);
        return ResponseEntity.ok(savedPlan);
    }

    // Update an existing
    @PutMapping("/{id}")
   @Secured("ADMIN")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id, @RequestBody Plan planDetails) {
        Optional<Plan> optionalPlan = planRepository.findById(id);
        if (optionalPlan.isPresent()) {
            Plan plan = optionalPlan.get();
            plan.setName(planDetails.getName());
            plan.setDescription(planDetails.getDescription());
            plan.setPlanType(planDetails.getPlanType());
            Plan updatedPlan = planRepository.save(plan);
            return ResponseEntity.ok(updatedPlan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a plan - Admin only
    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/purchase/{planId}")

    public ResponseEntity<String> purchasePlan(@PathVariable Long planId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Plan plan = planService.getPlanById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        user.setPlan(plan);
        userService.saveUser(user);
        return ResponseEntity.ok("Plan purchased successfully");
    }

    @GetMapping("/access/{courseId}")
    public ResponseEntity<String> accessCourse(@PathVariable Long courseId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        // Logic to check if the user has access to the course based on the plan
        boolean hasAccess = checkAccessToCourse(user, courseId);
        if (hasAccess) {
            return ResponseEntity.ok("Access granted");
        } else {
            return ResponseEntity.status(403).body("Access denied");
        }
    }

    private boolean checkAccessToCourse(User user, Long courseId) {
        if (user.getPlan() != null) {
            // If the user has a plan, they have access to the course
            return true;
        } else {
            // If the user does not have a plan, they do not have access to the course
            return false;
        }

    }

}
