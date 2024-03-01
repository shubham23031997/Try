package com.neosoft.transactionalexample;

import com.neosoft.transactionalexample.dto.FlightBookingAcknowledgement;
import com.neosoft.transactionalexample.dto.FlightBookingRequest;
import com.neosoft.transactionalexample.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableTransactionManagement
public class TransactionExampleApplication {

	@Autowired
	private FlightBookingService service;

	@PostMapping("Booking")
	public FlightBookingAcknowledgement bookingRequest(@RequestBody FlightBookingRequest request)
	{
		return service.bookingRequest(request);
	}

	public static void main(String[] args) {
		SpringApplication.run(TransactionExampleApplication.class, args);
	}

}