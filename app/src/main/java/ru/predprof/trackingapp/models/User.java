package ru.predprof.trackingapp.models;

public class User {
    double height = 1;
    double weight = 1;
    double imt = weight / (height * height);

    int personalSkills = 0;
    int healthStatus = 0;
}
