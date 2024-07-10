package com.main.service;


import org.springframework.stereotype.Service;

@Service
public interface UserServiceInfoEmail {
	
	
	
	public Integer sendMail( String email);
	public String otpgeneration(String email);
	public boolean isExpiredotp(Integer id);
	public String otpVerification(Integer id,String otp);
	public Integer resendOtpAgainService(Integer id);
	public void deleteExpiredOTP();


}
