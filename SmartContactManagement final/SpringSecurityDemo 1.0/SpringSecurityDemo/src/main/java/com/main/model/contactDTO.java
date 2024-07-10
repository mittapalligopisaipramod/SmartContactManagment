package com.main.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contactDTO {
	private Integer id;
	private String contactName;
	private BigDecimal contactNumber;
}
