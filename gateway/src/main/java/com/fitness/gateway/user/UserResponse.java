package com.fitness.gateway.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "keycloakId",
        "firstName",
        "lastName",
        "email",
        "createdAt",
        "updatedAt"
})
@Data
public class UserResponse {

    private String id;

    private String keycloakId;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
