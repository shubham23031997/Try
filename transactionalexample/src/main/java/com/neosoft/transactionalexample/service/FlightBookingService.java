package com.neosoft.transactionalexample.service;

import com.neosoft.transactionalexample.dto.FlightBookingAcknowledgement;
import com.neosoft.transactionalexample.dto.FlightBookingRequest;
import com.neosoft.transactionalexample.entity.PassengerInfo;
import com.neosoft.transactionalexample.entity.PaymentInfo;
import com.neosoft.transactionalexample.repository.PassengerInfoRepository;
import com.neosoft.transactionalexample.repository.PaymentInfoRepository;
import com.neosoft.transactionalexample.utils.PaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class FlightBookingService
{
    @Autowired
    private PassengerInfoRepository passengerInfoRepository;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;


    @Transactional//(readOnly=false,isolation=Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public FlightBookingAcknowledgement bookingRequest(FlightBookingRequest request)
    {
        //FlightBookingAcknowledgement acknowledgement;

        PassengerInfo passengerInfo = request.getPassengerInfo();
        passengerInfo = passengerInfoRepository.save(passengerInfo);

        PaymentInfo paymentInfo =request.getPaymentInfo();
        PaymentUtil.validateCreditLimit(paymentInfo.getAccountNo(), passengerInfo.getFare());

        paymentInfo.setPassengerId(passengerInfo.getPId());
        paymentInfo.setAmount(passengerInfo.getFare());

        paymentInfoRepository.save(paymentInfo);
        return new FlightBookingAcknowledgement("Success",passengerInfo.getFare(), UUID.randomUUID().toString().split("-")[0], passengerInfo);
    }
}
