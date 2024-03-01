package com.neosoft.transactionalexample.dto;

import com.neosoft.transactionalexample.entity.PassengerInfo;
import com.neosoft.transactionalexample.entity.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FlightBookingRequest {


    private PassengerInfo passengerInfo;


    private PaymentInfo paymentInfo;
}
