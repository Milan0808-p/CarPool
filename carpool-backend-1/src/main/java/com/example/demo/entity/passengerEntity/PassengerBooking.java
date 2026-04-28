package com.example.demo.entity.passengerEntity;

import java.time.*;
import java.util.UUID;

import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Journey;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "passenger_booking", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "journey_id" }) })
public class PassengerBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "public_id", unique = true, nullable = false, updatable = false)
    protected String publicId;
	
	// Passenger (User)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@ToString.Exclude
	private User passenger;

	// Journey
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "journey_id", nullable = false)
	@ToString.Exclude
	private Journey journey;

	// Booking details
	private Integer seatsBooked;

	private Double totalPrice;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	private String pickupPoint;
	private String dropPoint;

	private LocalDate travelDate;

	private LocalDateTime bookingTime;

	// Auto set booking time
	@PrePersist
	public void prePersist() {
		this.bookingTime = LocalDateTime.now();
		this.status = BookingStatus.PENDING;
		this.paymentStatus = PaymentStatus.PENDING;
		this.publicId = UUID.randomUUID().toString();
	}
	
}