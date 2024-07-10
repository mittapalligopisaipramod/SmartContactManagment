package com.main.model;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContactModel {
	private String contactName;
	private BigDecimal contactNumber;
}
