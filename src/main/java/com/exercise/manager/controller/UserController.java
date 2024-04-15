package com.exercise.manager.controller;

import com.exercise.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * user controller part
 * @author wby
 */
@RestController
public class UserController {


    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * Determine whether a user has permission to access resources
     * @param resource user access
     * @param userId userId
     * @return access result
     */
    @GetMapping("/user/{resource}")
    public String accessResource(@PathVariable String resource, @RequestAttribute("userId") Long userId) {
        // call service
        boolean accessGranted = userService.checkUserAccess(userId, resource);
        if (accessGranted) {
            log.info("access granted, user:" + userId);
            return "Access granted for resource: " + resource;
        }
        log.info("access denied, user:" + userId);
        return "Access denied for user:" + userId + " resource: " + resource;
    }
}

