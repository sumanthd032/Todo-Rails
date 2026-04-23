package com.todorails.controller;

import com.todorails.model.User;
import com.todorails.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // GET /register → show the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // empty User object for the form
        return "register";                       // show register.html
    }

    // POST /register → handle form submission
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {

        // @Valid triggers the annotations on User (@NotBlank, @Email, etc.)
        // BindingResult holds any validation errors

        if (result.hasErrors()) {
            return "register"; // re-show form with error messages
        }

        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError", "Username already taken");
            return "register";
        }

        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError", "Email already registered");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/login?registered"; // redirect to login after success
    }
}