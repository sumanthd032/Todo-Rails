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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/new")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("taskTypes", Task.TaskType.values());
        return "task-form";
    }

    @PostMapping("/new")
    public String saveTask(@Valid @ModelAttribute("task") Task task,
                           BindingResult result,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Task.Priority.values());
            model.addAttribute("taskTypes", Task.TaskType.values());
            return "task-form";
        }

        User user = userService.findByUsername(userDetails.getUsername());
        task.setUser(user);
        taskService.saveTask(task);

        redirectAttributes.addFlashAttribute("successMessage",
                "Task \"" + task.getTitle() + "\" added successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {

        Task task = taskService.getTaskById(id);

        if (!task.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("task", task);
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("taskTypes", Task.TaskType.values());
        model.addAttribute("editMode", true);
        return "task-form";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @Valid @ModelAttribute("task") Task updatedTask,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Task.Priority.values());
            model.addAttribute("taskTypes", Task.TaskType.values());
            model.addAttribute("editMode", true);
            return "task-form";
        }

        Task existingTask = taskService.getTaskById(id);

        if (!existingTask.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setTaskType(updatedTask.getTaskType());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompleted(updatedTask.isCompleted());

        taskService.saveTask(existingTask);

        redirectAttributes.addFlashAttribute("successMessage",
                "Task \"" + existingTask.getTitle() + "\" updated successfully!");
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        Task task = taskService.getTaskById(id);

        if (!task.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Task deleted successfully!");
        return "redirect:/dashboard";
    }
}