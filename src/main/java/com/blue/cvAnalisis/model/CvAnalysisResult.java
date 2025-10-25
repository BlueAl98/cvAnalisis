package com.blue.cvAnalisis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvAnalysisResult {
    private Candidate candidate;
    private Evaluation evaluation;
    @JsonProperty("key_points")
    private KeyPoints keyPoints;
    @JsonProperty("improvement_tips")
    private List<String> improvementTips;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Candidate {
    private String name;

    @JsonProperty("years_experience")
    private int yearsExperience;

    private String summary;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Evaluation {
    @JsonProperty("technical_match")
    private double technicalMatch;

    @JsonProperty("experience_match")
    private double experienceMatch;

    @JsonProperty("overall_score")
    private double overallScore;

    private String verdict;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class KeyPoints {
    private List<String> strengths;
    private List<String> weaknesses;

    @JsonProperty("recommended_questions")
    private List<String> recommendedQuestions;
}
