package com.todorails.controller;

import com.todorails.model.Task;
import com.todorails.model.User;
import com.todorails.service.TaskService;
import com.todorails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model) {

        // @AuthenticationPrincipal gives us the currently logged-in user
        // userDetails.getUsername() returns their username string
        User user = userService.findByUsername(userDetails.getUsername());

        List<Task> tasks = taskService.getTasksForUser(user);

        // pass data to the HTML template
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("totalTasks", tasks.size());
        model.addAttribute("completedTasks",
                tasks.stream().filter(Task::isCompleted).count());
        model.addAttribute("pendingTasks",
                tasks.stream().filter(t -> !t.isCompleted()).count());

        return "dashboard";
    }
}