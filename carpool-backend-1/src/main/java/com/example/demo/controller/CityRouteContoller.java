package com.example.demo.controller;

import com.example.demo.service.cityroute.RouteCityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class CityRouteContoller {

    private final RouteCityService routeCityService;

    public CityRouteContoller(RouteCityService routeCityService) {
        this.routeCityService = routeCityService;
    }

    @GetMapping("/cities")
    public List<String> getCities(
            @RequestParam double srcLat,
            @RequestParam double srcLng,
            @RequestParam double destLat,
            @RequestParam double destLng
    ) {
        return routeCityService.getCities(srcLat, srcLng, destLat, destLng);
    }

}