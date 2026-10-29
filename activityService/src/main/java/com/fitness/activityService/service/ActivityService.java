package com.fitness.activityService.service;

import com.fitness.activityService.dto.request.ActivityRequestDto;
import com.fitness.activityService.dto.response.ActivityResponseDto;
import com.fitness.activityService.entity.Activity;
import com.fitness.activityService.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponseDto trackActivity(ActivityRequestDto activityRequest) {

        Boolean isValidUser = userValidationService.validateUser(activityRequest.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid User: "+ activityRequest.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .duration(activityRequest.getDuration())
                .type(activityRequest.getType())
                .calorieBurned(activityRequest.getCalorieBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        //publish to rabbitmq for AI processing
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);

        }catch (Exception e){
            log.info("Failed to publish activity to RabbitMq: ", e);
        }
        return convertToDto(savedActivity);
    }


    public ActivityResponseDto getOneActivity(String Id) {
        return activityRepository.findById(Id)
                .map(this::convertToDto)
                .orElseThrow(()->new RuntimeException("Activity doesnot exist!"));
    }

    private ActivityResponseDto convertToDto(Activity activity){

        ActivityResponseDto activityResponseDto = new ActivityResponseDto();
        activityResponseDto.setId(activity.getId());
        activityResponseDto.setType(activity.getType());
        activityResponseDto.setUserId(activity.getUserId());
        activityResponseDto.setDuration(activity.getDuration());
        activityResponseDto.setCalorieBurned(activity.getCalorieBurned());
        activityResponseDto.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponseDto.setStartTime(activity.getStartTime());
        activityResponseDto.setCreatedAt(activity.getCreatedAt());
        activityResponseDto.setUpdatedAt(activity.getUpdatedAt());

        return activityResponseDto;
    }

//    public List<ActivityResponseDto> getActivities() {
//
//        List<Activity> activityList = activityRepository.findAll();
//
//
//        return activityList.stream()
//                .map(activity -> {
//                    ActivityResponseDto dto = convertToDto(activity);
//                    return dto;
//                })
//                .toList();
//    }

    public Page<ActivityResponseDto> getActivities(Pageable pageable) {

        return activityRepository.findAll(pageable)
                .map(activity -> convertToDto(activity));
    }

    public List<ActivityResponseDto> getUserActivities(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.
                stream().
                map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
