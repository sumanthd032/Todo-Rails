package com.todorails.service;

import com.todorails.model.Task;
import com.todorails.model.User;
import com.todorails.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // get all tasks for a user
    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserOrderByIdDesc(user);
    }

    // save a new task
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    // find a task by id
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // delete a task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}