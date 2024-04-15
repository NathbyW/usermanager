package com.exercise.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfo {

        private Long userId;
        private String accountName;
        private String role;

}
