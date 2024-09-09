package org.example.securingapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // This endpoint is only accessible to users with the role "USER" or "ADMIN"
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Welcome to the user dashboard!";
    }
}
