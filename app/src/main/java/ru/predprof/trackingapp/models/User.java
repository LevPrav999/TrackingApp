package ru.predprof.trackingapp.models;

public class User {
    double height = 1;
    double weight = 1;
    double imt = weight / (height * height);

    int personalSkills = 0;
    int healthStatus = 0;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getImt() {
        return imt;
    }

    public void setImt(double imt) {
        this.imt = imt;
    }

    public int getPersonalSkills() {
        return personalSkills;
    }

    public void setPersonalSkills(int personalSkills) {
        this.personalSkills = personalSkills;
    }

    public int getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(int healthStatus) {
        this.healthStatus = healthStatus;
    }
}
