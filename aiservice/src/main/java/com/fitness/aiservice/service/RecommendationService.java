package com.fitness.aiservice.service;

import com.fitness.aiservice.entity.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public Page<Recommendation> getUserRecommendation(String userId, Pageable pageable) {
       Page<Recommendation> recommendationList =
               recommendationRepository.findByUserId(userId,pageable);

       if(recommendationList.isEmpty()){
           throw new RuntimeException("No Recommedations for the user with id: "+userId);
       }

       return recommendationList;
    }

    public Recommendation getActivityRecommendation(String activityId) {
        Optional<Recommendation> recommendation = recommendationRepository.findByActivityId(activityId);
        if(recommendation.isEmpty()){
            throw new RuntimeException("No Recommendation for acitvity of id: "+ activityId);
        }
        return recommendation.get();
    }
}
