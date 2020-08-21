package util;

public class DistanceCalculator {
    private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    private DistanceCalculator() {
    }

    public static int calculateDistanceInKilometer(double sourceLat, double sourceLon, double destLat, double destLon) {
        double latDistance = Math.toRadians(sourceLat - destLat);
        double lngDistance = Math.toRadians(sourceLon - destLon);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(destLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
}
