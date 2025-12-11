package com.Eonline.Education.modals;

import com.Eonline.Education.user.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assignmentDescription;
    private LocalDate assignmentDate;
    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_group_id")
    private ChatGroup chatGroup;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskUser> users = new ArrayList<>();
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] file;
    private String name;
    private String type;
    private String traineeEmail;



}


//package com.Eonline.Education.modals;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
//@Entity
//public class Task {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    private String type;
//
//    private String taskName;
//
//    @Lob
//    @Column(name = "data", length = 1048576)
//    private byte[] data;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//}