package com.todorails.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// @ControllerAdvice means: apply this to ALL controllers
@ControllerAdvice
public class GlobalExceptionHandler {

    // catches RuntimeException from anywhere in the app
    // e.g. "Task not found" from TaskService.getTaskById()
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/500";
    }
}