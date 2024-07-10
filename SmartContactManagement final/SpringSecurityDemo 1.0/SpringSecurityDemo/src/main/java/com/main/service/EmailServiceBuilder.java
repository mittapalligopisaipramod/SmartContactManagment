package com.main.service;

import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceBuilder {
	private JavaMailSender javaMailSender;
	private com.main.repository.otpRepo otpRepo;
	
	public EmailServiceBuilder setJavaMailSender (JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
		return this;
	}
	
	public EmailServiceBuilder setOtp_repo (com.main.repository.otpRepo otpRepo) {
		this.otpRepo = otpRepo;
		return this;
	}
	
	public EmailServiceImp createEmailService () {
		return new EmailServiceImp (javaMailSender , otpRepo);
	}
}