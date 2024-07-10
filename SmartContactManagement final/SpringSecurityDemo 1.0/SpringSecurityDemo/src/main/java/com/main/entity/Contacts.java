package com.main.entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "contact_details")
@ToString (exclude = "user")
public class Contacts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-generated primary key for each contact
	private Integer id;
	private String contactName;
	@Column(nullable = false)
	private BigDecimal contactNumber;
	
	
	
	@ManyToOne
	@JoinColumn(name = "user_id") // Specifies the column for joining with the User table
	private User user;
}

