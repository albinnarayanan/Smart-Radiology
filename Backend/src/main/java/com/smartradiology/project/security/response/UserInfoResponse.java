package com.smartradiology.project.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String name;
    private String username;
    private String entityType;
    private String email;

    public UserInfoResponse(Long id, String name, String username, String entityType, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.entityType = entityType;
        this.email = email;
    }
}
