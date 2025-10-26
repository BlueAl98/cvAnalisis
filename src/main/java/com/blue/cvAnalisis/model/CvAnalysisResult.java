package com.blue.cvAnalisis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
public class CvAnalysisResult {
    private Candidate candidate;
    private Evaluation evaluation;
    @JsonProperty("key_points")
    private KeyPoints keyPoints;
    @JsonProperty("improvement_tips")
    private List<String> improvementTips;


    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public KeyPoints getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(KeyPoints keyPoints) {
        this.keyPoints = keyPoints;
    }

    public List<String> getImprovementTips() {
        return improvementTips;
    }

    public void setImprovementTips(List<String> improvementTips) {
        this.improvementTips = improvementTips;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}

