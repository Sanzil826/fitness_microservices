package com.fitness.aiservice.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Activity {
        private String id;
        private String userId;
        private Integer duration;
        private Integer calorieBurned;
        private LocalDateTime startTime;
        private Map<String,Object> additionalMetrics;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
