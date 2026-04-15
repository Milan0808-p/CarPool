package com.example.demo.service.cityroute;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OsrmRouteService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<List<Double>> getRoute(double srcLat, double srcLng,
                                       double destLat, double destLng) {

        String url = "https://router.project-osrm.org/route/v1/driving/"
                + srcLng + "," + srcLat + ";"
                + destLng + "," + destLat
                + "?overview=full&geometries=geojson";

        Map response = restTemplate.getForObject(url, Map.class);

        List routes = (List) response.get("routes");
        Map route = (Map) routes.get(0);
        Map geometry = (Map) route.get("geometry");

        return (List<List<Double>>) geometry.get("coordinates");
    }
}
