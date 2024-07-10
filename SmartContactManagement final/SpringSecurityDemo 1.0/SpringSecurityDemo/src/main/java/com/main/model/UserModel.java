package com.main.model;


import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {
	private String user_name;
	private String password;
}
