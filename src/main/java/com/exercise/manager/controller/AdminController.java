package com.exercise.manager.controller;

import com.exercise.manager.model.UserAccessInfo;
import com.exercise.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * admin controller, for
 * @author wby
 */
@RestController
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    public static final String ADMIN = "admin";

    public static final String USER = "user";

    /**
     * addUser method for adding resource
     * @param role user role
     * @param accessInfo access information
     * @return add result
     */
    @PostMapping("/admin/addUser")
    public String addUser(@RequestAttribute("role") String role, @RequestBody UserAccessInfo accessInfo) {

        //  user judgement, if not admin return failure.
        if (!ADMIN.equals(role)) {
            log.error(" Only admins can add users.");
            return "Access denied. Only admins can add users.";
        }
        try {
            // call add service and reserve information in file
            userService.addUserAccess(accessInfo.getUserId(), accessInfo.getEndpoint());
        } catch (Exception e) {
            log.error("Failed to add user resource", e);
            return "An error occurred while adding user access.";
        }
        return "User access updated.";
    }

}
