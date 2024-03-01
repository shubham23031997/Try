package com.payment.ms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Payment {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String mode;

	@Column
	private Long orderId;

	@Column
	private double amount;

	@Column
	private String status;

}
