package com.example.demo.service.chat;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // SEND NOTIFICATION TO EACH PASSENGER
    public void notifyPassengers(List<String> passengerIds, String rideId) {

        for (String passengerId : passengerIds) {

            messagingTemplate.convertAndSendToUser(
                    passengerId,
                    "/queue/notifications",
                    " Your ride " + rideId + " has started!"
            );
        }
    }
}