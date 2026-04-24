package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    private final String URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ Generic method (MAIN)
    public void sendEmail(String toEmail, String subject, String htmlContent) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of(
                "name", "Carpool",
                "email", "milanprajapati0808@gmail.com" // ⚠️ verify in Brevo
        ));

        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(URL, request, String.class);

            System.out.println("✅ Email sent: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("❌ Email failed: " + e.getMessage());
        }
    }

    //  Booking Notifications

    public void notifyDriver(String driverEmail, String pickup, String drop) {

        String subject = "🚗 New Booking Request";

        String html = "<h3>You have a new booking request</h3>"
                + "<p><b>Pickup:</b> " + pickup + "</p>"
                + "<p><b>Drop:</b> " + drop + "</p>";

        sendEmail(driverEmail, subject, html);
    }

    public void notifyPassenger(String passengerEmail) {

        String subject = "✅ Booking Confirmed";

        String html = "<h3>Your booking is confirmed!</h3>"
                + "<p>Driver will contact you soon 🚗</p>";

        sendEmail(passengerEmail, subject, html);
    }

    // Optional OTP reuse
    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "Your OTP Code";
        String html = "<h3>Your OTP is: " + otp + "</h3><p>Valid for 5 minutes.</p>";

        sendEmail(toEmail, subject, html);
    }
}