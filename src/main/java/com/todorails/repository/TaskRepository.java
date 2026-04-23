package com.todorails.repository;

import com.todorails.model.Task;
import com.todorails.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // get all tasks for a specific user, newest first
    List<Task> findByUserOrderByIdDesc(User user);

    // get tasks filtered by completion status
    List<Task> findByUserAndCompletedOrderByIdDesc(User user, boolean completed);
}