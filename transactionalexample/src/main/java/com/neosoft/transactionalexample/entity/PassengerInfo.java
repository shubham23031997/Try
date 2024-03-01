package com.neosoft.transactionalexample.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class PassengerInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pId;

    private String name;

    private String email;

    private String source;

    private String destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date travelDate;

    //@Column
    private String pickUpTime;

    //@Column
    private String arrivalTime;

    //@Column
    private double fare;
}
