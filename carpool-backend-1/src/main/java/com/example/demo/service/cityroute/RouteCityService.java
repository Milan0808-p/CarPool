package com.example.demo.service.cityroute;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RouteCityService {
    private final OsrmRouteService osrmRouteService;
    private final FreeGeocodeService geocodeService;

    // ✅ Thread-safe cache
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    // ✅ Thread pool for parallel calls
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public RouteCityService(OsrmRouteService osrmRouteService,
                            FreeGeocodeService geocodeService) {
        this.osrmRouteService = osrmRouteService;
        this.geocodeService = geocodeService;
    }

    public List<String> getCities(double srcLat, double srcLng,
                                  double destLat, double destLng) {

        Set<String> cities = new LinkedHashSet<>();

        // ✅ 1. ADD START CITY
        String startKey = srcLat + "," + srcLng;
        String startCity = cache.containsKey(startKey)
                ? cache.get(startKey)
                : geocodeService.getCity(srcLat, srcLng);

        cache.put(startKey, startCity);

        if (startCity != null && !startCity.equalsIgnoreCase("unknown")) {
            cities.add(startCity);
        }

        // ✅ 2. GET ROUTE
        List<List<Double>> route = osrmRouteService.getRoute(
                srcLat, srcLng, destLat, destLng
        );

        // ✅ 3. SAMPLE MIDDLE POINTS
        List<double[]> sampledPoints = samplePoints(route, 20);

        List<CompletableFuture<String>> futures = sampledPoints.stream()
                .map(point -> CompletableFuture.supplyAsync(() -> {

                    double lat = point[0];
                    double lon = point[1];

                    String key = lat + "," + lon;

                    if (cache.containsKey(key)) {
                        return cache.get(key);
                    }

                    String city = geocodeService.getCity(lat, lon);
                    cache.put(key, city);

                    return city;

                }, executor))
                .toList();

        List<String> midCities = futures.stream()
                .map(CompletableFuture::join)
                .filter(city -> city != null && !city.equalsIgnoreCase("unknown"))
                .toList();

        cities.addAll(midCities);

        // ✅ 4. ADD END CITY
        String endKey = destLat + "," + destLng;
        String endCity = cache.containsKey(endKey)
                ? cache.get(endKey)
                : geocodeService.getCity(destLat, destLng);

        cache.put(endKey, endCity);

        if (endCity != null && !endCity.equalsIgnoreCase("unknown")) {
            cities.add(endCity);
        }

        return new ArrayList<>(cities);
    }

    // ✅ Distance-based sampling
    private List<double[]> samplePoints(List<List<Double>> route, double stepKm) {

        List<double[]> result = new ArrayList<>();

        double accumulated = 0;

        for (int i = 1; i < route.size(); i++) {

            double lat1 = route.get(i - 1).get(1);
            double lon1 = route.get(i - 1).get(0);

            double lat2 = route.get(i).get(1);
            double lon2 = route.get(i).get(0);

            double dist = haversine(lat1, lon1, lat2, lon2);
            accumulated += dist;

            if (accumulated >= stepKm) {
                result.add(new double[]{lat2, lon2});
                accumulated = 0;
            }
        }

        return result;
    }

    // ✅ Haversine formula
    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {

        final double R = 6371; // Earth radius (km)

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


}
