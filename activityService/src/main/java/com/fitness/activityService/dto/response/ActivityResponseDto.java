package com.fitness.activityService.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fitness.activityService.entity.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@JsonPropertyOrder({
        "id",
        "userId",
        "type",
        "startTime",
        "duration",
        "calorieBurned",
        "additionalMetrics",
        "createdAt",
        "updatedAt"
})
@Data
public class ActivityResponseDto {
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer calorieBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
