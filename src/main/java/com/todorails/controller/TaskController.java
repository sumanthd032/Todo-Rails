package com.todorails.controller;

import com.todorails.model.Task;
import com.todorails.model.User;
import com.todorails.service.TaskService;
import com.todorails.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")   // all routes in this controller start with /tasks
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    // GET /tasks/new → show the add task form
    @GetMapping("/new")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("taskTypes", Task.TaskType.values());
        return "task-form";
    }

    // POST /tasks/new → save the task
    @PostMapping("/new")
    public String saveTask(@Valid @ModelAttribute("task") Task task,
                           BindingResult result,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {

        if (result.hasErrors()) {
            // re-populate dropdowns if validation fails
            model.addAttribute("priorities", Task.Priority.values());
            model.addAttribute("taskTypes", Task.TaskType.values());
            return "task-form";
        }

        // link this task to the logged-in user
        User user = userService.findByUsername(userDetails.getUsername());
        task.setUser(user);

        taskService.saveTask(task);
        return "redirect:/dashboard";
    }
}