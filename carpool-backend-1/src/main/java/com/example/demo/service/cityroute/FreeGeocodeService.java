package com.example.demo.service.cityroute;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FreeGeocodeService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String getCity(double lat, double lon) {

        try {
            String url = "https://nominatim.openstreetmap.org/reverse"
                    + "?format=json"
                    + "&lat=" + lat
                    + "&lon=" + lon;

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "carpool-app");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map body = response.getBody();
            if (body == null) return null;

            Map address = (Map) body.get("address");
            if (address == null) return null;

            if (address.get("city") != null)
                return address.get("city").toString();

            if (address.get("town") != null)
                return address.get("town").toString();

            if (address.get("village") != null)
                return address.get("village").toString();

            if (address.get("county") != null)
                return address.get("county").toString();

            if (address.get("state_district") != null)
                return address.get("state_district").toString();

        } catch (Exception e) {
            System.out.println("Geocode error: " + e.getMessage());
        }

        return "unknown";
    }
}
