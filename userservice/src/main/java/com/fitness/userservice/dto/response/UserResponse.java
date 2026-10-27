package com.fitness.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fitness.userservice.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "firstName",
        "lastName",
        "email",
        "createdAt",
        "updatedAt"
})
@Data
public class UserResponse {

    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
