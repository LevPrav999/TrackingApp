package ru.predprof.trackingapp.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapUtils {

    public static LatLng findNearestLatLng(List<LatLng> latLngArray, LatLng targetPoint) {
        double minDistance = 0.01d;
        LatLng nearestLatLng = null;

        for (LatLng latLng : latLngArray) {
            double distance = haversineDistance(latLng.latitude, latLng.longitude, targetPoint.latitude, targetPoint.longitude);
            if (distance < minDistance) {
                minDistance = distance;
                nearestLatLng = latLng;
            }
        }

        return nearestLatLng;
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public double countDistanceBetweenToPoints(double lat1, double long1, double lat2, double long2) {
        // 1 - конец
        // 2 - начало
        double a = Math.abs(lat1 - lat2);
        double b = Math.abs(long2 - long1);

        return Math.sqrt(a * a + b * b);
    }
}
