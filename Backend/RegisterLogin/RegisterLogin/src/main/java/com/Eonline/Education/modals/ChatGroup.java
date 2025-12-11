package com.Eonline.Education.modals;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chat_group_users",
            joinColumns = @JoinColumn(name = "chat_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private TraineeCredentialGenerator trainees;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meeting> meetings = new ArrayList<>();
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;

    // Constructors, getters, and setters

    public ChatGroup() {
    }

    public void addUser(User user) {
        if (!this.members.contains(user)) {
            this.members.add(user);
        }
    }
    public ChatGroup(String name) {
        this.name = name;
    }

}
