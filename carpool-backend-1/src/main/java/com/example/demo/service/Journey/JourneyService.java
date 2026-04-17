package com.example.demo.service.Journey;
import com.example.demo.dto.journeyDtos.CreateJourneyDTO;
import com.example.demo.entity.driverEntity.Driver;
import com.example.demo.entity.journeyEntity.Journey;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final DriverRepository driverRepository;


    public CreateJourneyDTO createJourney(CreateJourneyDTO request){

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Journey journey = new Journey();
        journey.setStartLocation(request.getStartLocation());
        journey.setEndLocation(request.getEndLocation());
        journey.setAvailableSeats(request.getAvailableSeats());
        journey.setPrice(request.getPrice());
        journey.setDate(request.getDate());
        journey.setDepartureTime(request.getDepartureTime());
        journey.setDriver(driver);

        // ✅ Store stops
        journey.setStops(convertListToString(request.getStops()));

        // ✅ Generate embedding (IMPORTANT FORMAT)
        String embedding = generateEmbedding(request.getStops());
        journey.setRouteEmbedding(embedding);

        journeyRepository.save(journey);

        return request;
    }

    private String convertListToString(List<String> stops) {
        return String.join(",", stops);
    }

    private String generateEmbedding(List<String> stops) {
        return "[0.12,0.45,0.67,0.89,0.23,0.78]";
    }

    public List<Journey> searchSimilarRoutes(List<Double> queryVector) {
        String vector = queryVector.toString(); // "[0.1,0.2,...]"
        return journeyRepository.findNearest(vector);
    }

}
