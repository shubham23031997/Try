package com.orchestractionservice.orchestractionservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {

    private String type;

    private CustomerOrder customerOrder;

}
