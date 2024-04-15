package com.exercise.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Basic data object for both administrators and users.
 * @author wby
 */
@Getter
@Setter
@ToString
public class UserAccessInfo {


    /**
     * userId
     */
    private Long userId;

    /**
     * list of endpoints that user is grand to access
     */
    private List<String> endpoint;

    /**
     * Constructor without parameters
     */
    public UserAccessInfo() {

    }

    /**
     * Constructor
     */
    public UserAccessInfo(Long userId, List<String> endpoint) {
        this.userId = userId;
        this.endpoint = endpoint;
    }
}
