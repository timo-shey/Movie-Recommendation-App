package com.example.moviesearchapplication.utils;

import com.example.moviesearchapplication.exception.UserNotFoundException;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppUtil {
    private final UserRepository userRepository;

    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(((UserDetails)principal).getUsername())
                .orElseThrow(() -> new UserNotFoundException("Error getting logged in user"));
    }
}
