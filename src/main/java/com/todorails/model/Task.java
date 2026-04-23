package com.todorails.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    // Enum stored as string in DB ("HIGH" not 0/1/2)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType = TaskType.PERSONAL;

    private LocalDate dueDate;

    private boolean completed = false;

    // Many tasks belong to one user
    // @JoinColumn creates the user_id foreign key column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Enum for priority levels
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // Enum for task types
    public enum TaskType {
        PERSONAL, OFFICIAL, STUDY, HEALTH, OTHER
    }
}