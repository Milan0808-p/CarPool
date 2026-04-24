package com.example.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.dto.kafka.BookingEvent;
import com.example.demo.service.EmailService;

@Service
public class BookingConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "booking-topic", groupId = "booking-group")
    public void consume(BookingEvent event) {

        System.out.println("📩 Event received: " + event.getStatus());

        switch (event.getStatus()) {

            case "PENDING":
                emailService.notifyDriver(
                        event.getDriverEmail(),
                        event.getPickupPoint(),
                        event.getDropPoint()
                );
                break;

            case "CONFIRMED":
                emailService.notifyPassenger(event.getPassengerEmail());
                break;

            case "REJECTED":
                emailService.notifyPassenger(event.getPassengerEmail());
                break;

            default:
                System.out.println("Unknown status");
        }
    }
}
