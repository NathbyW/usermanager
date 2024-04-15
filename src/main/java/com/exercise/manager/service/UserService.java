package com.exercise.manager.service;

import com.exercise.manager.model.UserAccessInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * main service for user and admins.
 * @author wby
 */
@Service
public class UserService {
    /**
     * Using logback to write logs for some error or exception
     */
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String ACCESS_FILE_PATH = "E:/program/manager/accessRecord.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * read access record from file(using database normally)
     * @return list of user information
     */
    public List<UserAccessInfo> loadAccessData() {
        try {
            File file = new File(ACCESS_FILE_PATH);
            // corner case, when file not exists or is empty
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(file, new TypeReference<List<UserAccessInfo>>() {});

        } catch (IOException e) {
            log.error("Failed to load access data from file: " + ACCESS_FILE_PATH, e);
        }
        return new ArrayList<>();
    }

    public void saveAccessData(List<UserAccessInfo> userAccessList) throws IOException {
        try {
            objectMapper.writeValue(new File(ACCESS_FILE_PATH), userAccessList);
        } catch (Throwable e) {
            log.error("Failed to save access data to file: " + ACCESS_FILE_PATH, e);
        }
    }

    /**
     *
     * @param userId access id
     * @param resource access resource
     * @return true for granted and false for not authorized
     */
    public boolean checkUserAccess (Long userId, String resource) {
        try {
            List<UserAccessInfo> userAccessList = loadAccessData();
            Optional<UserAccessInfo> userAccess = userAccessList.stream()
                    .filter(ua -> ua.getUserId().equals(userId) && ua.getEndpoint().contains(resource))
                    .findFirst();
            return userAccess.isPresent();
        } catch (Exception e) {
            log.error("Error checking user access for userId: " + userId + " and resource: " + resource, e);
            return false;
        }
    }

    /**
     *
     * @param userId useId added
     * @param resourceList resource user has been granted
     */
    public void addUserAccess(Long userId, List<String> resourceList) {
        try {
            // load existing data from record file.
            List<UserAccessInfo> userAccessList = loadAccessData();
            Optional<UserAccessInfo> existingAccess = userAccessList
                    .stream()
                    .filter(access -> access.getUserId().equals(userId))
                    .findFirst();

            if (existingAccess.isPresent()) {
                // if user or resource exists, update the resource instead of inserting
                List<String> existingResources = existingAccess.get().getEndpoint();
                for (String resource : resourceList) {
                    if (!existingResources.contains(resource)) {
                        existingResources.add(resource);
                    }
                }
            } else {
                userAccessList.add(new UserAccessInfo(userId, resourceList));
            }

            saveAccessData(userAccessList);
        } catch (Exception e) {
            log.error("Failed to add user access for userId: " + userId, e);
        }
    }
}
