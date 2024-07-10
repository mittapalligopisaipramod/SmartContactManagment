package com.main.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTP {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	private String otp;
	//private Date exp;
	@Column(nullable = false)
	private String email;
	//private Date created;
	@Column(name = "created_time")
	private LocalDateTime createdTime;
	
	@Column(name = "expired_time")
	private LocalDateTime expiredTime;
}
