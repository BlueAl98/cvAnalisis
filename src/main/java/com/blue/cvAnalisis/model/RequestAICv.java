package com.blue.cvAnalisis.model;

public class RequestAICv {
    String cvText;
    String targetProfile;

    public RequestAICv() {
    }
    public RequestAICv(String cvText, String targetProfile) {
        this.cvText = cvText;
        this.targetProfile = targetProfile;
    }
    public String getCvText() {
        return cvText;
    }
    public void setCvText(String cvText) {
        this.cvText = cvText;
    }
    public String getTargetProfile() {
        return targetProfile;
    }
    public void setTargetProfile(String targetProfile) {
        this.targetProfile = targetProfile;
    }

}
