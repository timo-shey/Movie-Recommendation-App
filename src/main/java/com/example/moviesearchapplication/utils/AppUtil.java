package com.example.moviesearchapplication.utils;

import com.example.moviesearchapplication.exception.UserNotFoundException;
import com.example.moviesearchapplication.model.User;
import com.example.moviesearchapplication.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(AppUtil.class);

    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

}
