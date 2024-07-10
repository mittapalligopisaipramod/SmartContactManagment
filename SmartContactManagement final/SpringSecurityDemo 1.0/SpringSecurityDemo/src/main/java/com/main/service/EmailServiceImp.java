package com.main.service;

import com.main.entity.OTP;
import com.main.repository.otpRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements UserServiceInfoEmail {
	
	private final otpRepo otp_repo;
	private  final JavaMailSender javaMailSender;
	
	@Autowired
	public EmailServiceImp (JavaMailSender javaMailSender , otpRepo otp_repo) {
		this.javaMailSender = javaMailSender;
		this.otp_repo = otp_repo;
	}
	
	
	
	
	@Transactional
	public Integer sendMail( String email){
		
		String otp_sended=otpgeneration(email);
		SimpleMailMessage message=new SimpleMailMessage ();
		message.setFrom(sender);
		message.setTo(email);
		message.setSubject("OTP");
		message.setText("this is the otp for account verification :"+otp_sended +" thank you for using our service");
		
		javaMailSender.send(message);
		System.out.println ("otp is sended");
		OTP otp_data= OTP.builder()
				.otp (otp_sended)
				.email (email)
				.expiredTime (LocalDateTime.now ().plusMinutes (5))
				.createdTime (LocalDateTime.now ())
				.build();
		OTP saved = otp_repo.save (otp_data);
		System.out.println ("otp is saved");
		Optional<OTP> byEmail = otp_repo.findByEmail (email);
		if(byEmail.isPresent()){
			System.out.println ("otp is present");
			System.out.println (byEmail.get ().getId ());
			return  (byEmail.get ().getId ());
		}
		else
			System.out.println ("otp is not present");
		
		return -1;
		
	}


	
	
	@Value ("${spring.mail.username}")
	private String sender;
	
	public String otpgeneration(String email){
		Random random=new Random ();
		Integer number=10000+random.nextInt(999999);
		return String.valueOf(number);
	}
	
	public boolean isExpiredotp(Integer id){
		
		Optional<OTP> otpOptional = otp_repo.findById(id);
		if (otpOptional.isPresent()) {
			LocalDateTime exp = otpOptional.get().getExpiredTime();
			return exp.isAfter (LocalDateTime.now ());
		}
		return false;
	}
	
	
	public String otpVerification(Integer id,String otp){
		if(isExpiredotp(id)){
			Optional<OTP> id1 = otp_repo.findById (id);
			if(id1.isPresent ()){
				if(id1.get().getOtp().equals(otp)){
					
					return "ok";
				}
				else{
					return "wrong otp";
				}
			}
		}
		return "check the otp again";
	}
	
	
	public Integer resendOtpAgainService(Integer id){
		System.out.println ("enter into resend otp session");
		Optional<OTP> id1 = otp_repo.findById (id);
		String re_mail=id1.get ().getEmail ();
		otp_repo.deleteById (id);
		String otp=otpgeneration(re_mail);
		SimpleMailMessage message=new SimpleMailMessage ();
		message.setFrom(sender);
		message.setTo(re_mail);
		message.setSubject("OTP");
		message.setText(otp);
		javaMailSender.send(message);
		OTP otp_data= OTP.builder()
				.email (re_mail)
				.expiredTime (LocalDateTime.now ().plusMinutes (5))
				.createdTime (LocalDateTime.now ())
				.otp (otp)
				.build ();
		OTP saved = otp_repo.save (otp_data);
		System.out.println ("otp is saved");
		return saved.getId ();
	}
	
	@Scheduled (fixedRate = 3_00_000)
	public void deleteExpiredOTP() {
		LocalDateTime currentTime = LocalDateTime.now();
		List<OTP> expiredOTPList = otp_repo.findByExpiredTimeBefore(currentTime);
		otp_repo.deleteAll(expiredOTPList);
	}
	
	
}


/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public Integer sendMail(String email) {
    try {
        String otp_sended = otpgeneration(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject("OTP");
        message.setText("This is the OTP for account verification: " + otp_sended + " Thank you for using our service.");

        javaMailSender.send(message);
        System.out.println("OTP is sent");

        OTP otp_data = OTP.builder()
                .otp(otp_sended)
                .email(email)
                .expiredTime(LocalDateTime.now().plusMinutes(5))
                .createdTime(LocalDateTime.now())
                .build();

        OTP saved = otp_repo.save(otp_data);
        System.out.println("OTP is saved");

        Optional<OTP> byEmail = otp_repo.findByEmail(email);
        if (byEmail.isPresent()) {
            System.out.println("OTP is present");
            System.out.println(byEmail.get().getId());
            return byEmail.get().getId();
        } else {
            System.out.println("OTP is not present");
            return -1;
        }
    } catch (MailException e) {
        System.err.println("Mail sending failed: " + e.getMessage());
        return -1;
    }
}

 */

