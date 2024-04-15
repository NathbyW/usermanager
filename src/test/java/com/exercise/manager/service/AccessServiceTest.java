package com.exercise.manager.service;

import com.exercise.manager.model.UserAccessInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private ObjectMapper objectMapper;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoadAccessDataWithNoFile() throws Exception {
        lenient().when(objectMapper.readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(new ArrayList<>());
        Assertions.assertTrue(userService.loadAccessData().isEmpty());
    }

    @Test
    public void testAddUserAccess_NewUser() throws IOException {
        Long userId = 1L;
        List<String> resources = Arrays.asList("resourceA");
        List<UserAccessInfo> infoList = new ArrayList<>();
        infoList.add(new UserAccessInfo(userId, resources));

        userService.saveAccessData(infoList);

        // Verify that objectMapper.writeValue is called once to save the updated list
        verify(objectMapper, times(0)).readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(TypeReference.class));
    }

    @Test
    public void testAddUserAccess_ExistingUser() throws IOException {
        Long userId = 1L;
        List<String> existingResources = Collections.singletonList("resourceA");
        UserAccessInfo userAccess = new UserAccessInfo(userId, new ArrayList<>(existingResources));
        List<UserAccessInfo> existingAccesses = Collections.singletonList(userAccess);

        when(objectMapper.readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(TypeReference.class))).thenReturn(existingAccesses);

        List<String> newResources = Arrays.asList("resourceB");
        userService.addUserAccess(userId, newResources);

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(objectMapper).writeValue(ArgumentMatchers.any(File.class), argument.capture());

        List<UserAccessInfo> updatedAccesses = argument.getValue();
        Assertions.assertNotNull(updatedAccesses);
        Assertions.assertFalse(updatedAccesses.isEmpty());
        Assertions.assertEquals(2, updatedAccesses.get(0).getEndpoint().size());
        Assertions.assertTrue(updatedAccesses.get(0).getEndpoint().containsAll(Arrays.asList("resourceA", "resourceB")));
    }

    @Test
    public void testAddUserAccess_DuplicateResource() throws IOException {
        Long userId = 1L;
        List<String> existingResources = Arrays.asList("resourceA");
        UserAccessInfo userAccess = new UserAccessInfo(userId, new ArrayList<>(existingResources));
        List<UserAccessInfo> existingAccesses = Arrays.asList(userAccess);

        when(objectMapper.readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(existingAccesses);

        // Trying to add the same resource again
        List<String> newResources = Arrays.asList("resourceA");
        userService.addUserAccess(userId, newResources);

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(objectMapper).writeValue(ArgumentMatchers.any(File.class), argument.capture());

        List<UserAccessInfo> updatedAccesses = argument.getValue();
        Assertions.assertNotNull(updatedAccesses);
        Assertions.assertFalse(updatedAccesses.isEmpty());

        // Size should still be 1
        Assertions.assertEquals(1, updatedAccesses.get(0).getEndpoint().size());
        Assertions.assertTrue(updatedAccesses.get(0).getEndpoint().contains("resourceA"));
    }


}
