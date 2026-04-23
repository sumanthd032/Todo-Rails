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

    // GET /tasks/edit/{id} → show form pre-filled with task data
    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {

        Task task = taskService.getTaskById(id);

        // SECURITY CHECK: make sure this task belongs to the logged-in user
        // If user A tries to edit user B's task via URL manipulation, block them
        if (!task.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("task", task);           // pre-filled task
        model.addAttribute("priorities", Task.Priority.values());
        model.addAttribute("taskTypes", Task.TaskType.values());
        model.addAttribute("editMode", true);        // tell HTML we are editing
        return "task-form";
    }

    // POST /tasks/edit/{id} → save the updated task
    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @Valid @ModelAttribute("task") Task updatedTask,
                             BindingResult result,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("priorities", Task.Priority.values());
            model.addAttribute("taskTypes", Task.TaskType.values());
            model.addAttribute("editMode", true);
            return "task-form";
        }

        // fetch the ORIGINAL task from DB
        Task existingTask = taskService.getTaskById(id);

        // security check again
        if (!existingTask.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        // update only the fields the user can change
        // we do NOT change id or user — those stay the same
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setTaskType(updatedTask.getTaskType());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompleted(updatedTask.isCompleted());

        taskService.saveTask(existingTask);
        return "redirect:/dashboard";
    }

    // POST /tasks/delete/{id} → delete the task
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Task task = taskService.getTaskById(id);

        if (!task.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        taskService.deleteTask(id);

        // addFlashAttribute survives ONE redirect then disappears automatically
        redirectAttributes.addFlashAttribute("successMessage", "Task deleted successfully!");
        return "redirect:/dashboard";
    }
}