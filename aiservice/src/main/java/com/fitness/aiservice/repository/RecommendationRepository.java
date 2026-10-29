package com.fitness.aiservice.repository;

import com.fitness.aiservice.entity.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    Page<Recommendation> findByUserId(String userId, Pageable pageable);

    Optional<Recommendation> findByActivityId(String activityId);
}
