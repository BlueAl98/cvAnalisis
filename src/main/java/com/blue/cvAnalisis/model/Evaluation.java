package com.blue.cvAnalisis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @JsonProperty("technical_match")
    private double technicalMatch;

    @JsonProperty("experience_match")
    private double experienceMatch;

    @JsonProperty("overall_score")
    private double overallScore;

    private String verdict;

    public double getTechnicalMatch() {
        return technicalMatch;
    }

    public void setTechnicalMatch(double technicalMatch) {
        this.technicalMatch = technicalMatch;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getExperienceMatch() {
        return experienceMatch;
    }

    public void setExperienceMatch(double experienceMatch) {
        this.experienceMatch = experienceMatch;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }
}
