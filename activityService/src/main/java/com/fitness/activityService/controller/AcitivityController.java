package com.fitness.activityService.controller;

import com.fitness.activityService.dto.request.ActivityRequestDto;
import com.fitness.activityService.dto.response.ActivityResponseDto;
import com.fitness.activityService.entity.Activity;
import com.fitness.activityService.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class AcitivityController {

    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponseDto> trackActivity(@RequestBody ActivityRequestDto activityRequest){
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> getOneActivity(@PathVariable String id){
        return ResponseEntity.ok(activityService.getOneActivity(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getUserActivities(@RequestHeader("X-USER-ID") String userId){
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ActivityResponseDto>> getAllActivities(
            @PageableDefault(size = 2) Pageable pageable) {

        Sort sort = Sort.by(
                Sort.Direction.DESC, "createdAt")
                .and(Sort.by(
                Sort.Direction.DESC, "updatedAt"
        ));

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        return ResponseEntity.ok(
                activityService.getActivities(sortedPageable)
        );
    }
}
