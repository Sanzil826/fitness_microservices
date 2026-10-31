package com.fitness.aiservice.service;

import com.fitness.aiservice.entity.Activity;
import com.fitness.aiservice.entity.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("Response from AI: {}", aiResponse);
        return processAiResponse(activity, aiResponse);

    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("steps")
                    .get(1)
                    .path("content")
                    .get(0)
                    .path("text");
            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();
//            log.info("Parsed Response from AI: {}", jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "OverAll:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRace", "HeartRace:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "CaloriesBurned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggesitons(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a proffesional for help"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item -> {
                safety.add(item.asText());
            });
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safety;
    }

    private List<String> extractSuggesitons(JsonNode suggestionNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionNode.isArray()){
            suggestionNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        JsonNode value = analysisNode.path(key);

        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(value.asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity){
        return String.format("""
                Analyze this fitness activity and provide detailed recommendation in the following exact JSON format:
                 {
                    "analysis": {
                        "overall" : "Overall analysis here",
                        "pace" : "Pace analysis here",
                        "heartRace" : "Heart rate analysis here",
                        "caloriesBurned" : "Calories analysis here"
                        },
                    "improvements": [
                        {
                            "area": "Area name",
                            "recommendation": "Detailed Recommendation"
                        }
                        ],
                    "suggestions": [
                        {
                            "workout": "Workout name",
                            "description": "Detailed workout description"
                        }
                        ],
                    "safety":[
                            "safety point 1",
                            "safety point 2"
                        ]
                 }
                 Analyze this acitvity:
                 Activity Type: %s,
                 Duration: %d minutes,
                 Calories Burned: %d,
                 Additional Metrics: %s
                 
                 Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                  Ensure the response follows the EXACT JSON format shown above.
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCalorieBurned(),
                activity.getAdditionalMetrics());
    }
}
