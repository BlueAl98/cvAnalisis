package com.blue.cvAnalisis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class KeyPoints {
    private List<String> strengths;
    private List<String> weaknesses;

    @JsonProperty("recommended_questions")
    private List<String> recommendedQuestions;

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getRecommendedQuestions() {
        return recommendedQuestions;
    }

    public void setRecommendedQuestions(List<String> recommendedQuestions) {
        this.recommendedQuestions = recommendedQuestions;
    }
}
