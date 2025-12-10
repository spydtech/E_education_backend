package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    public Plan(PlanType planType, String basicPlanDescription) {
    }

    // Enum for Plan Types
    public enum PlanType {
        BASIC, PREMIUM, ADVANCED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    // Custom constructor
    public Plan(PlanType planType, String description, int price) {
        this.planType = planType;
        this.name = planType.name();
        this.description = description;
        this.price = price;
    }
}
