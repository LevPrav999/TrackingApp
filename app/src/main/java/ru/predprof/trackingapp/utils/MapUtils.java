package ru.predprof.trackingapp.utils;

public class MapUtils {

    public double countDistanceBetweenToPoints(double lat1, double long1, double lat2, double long2) {
        // 1 - конец
        // 2 - начало
        double a = Math.abs(lat1-lat2);
        double b = Math.abs(long2-long1);

        return Math.sqrt(a*a+b*b);
    }
}
