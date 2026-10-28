package com.fitness.activityService.dto.request;

import com.fitness.activityService.entity.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequestDto {

    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer calorieBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;
}
