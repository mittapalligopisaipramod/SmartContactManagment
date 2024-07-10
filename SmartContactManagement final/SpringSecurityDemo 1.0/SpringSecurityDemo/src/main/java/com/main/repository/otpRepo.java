package com.main.repository;


import com.main.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface otpRepo extends JpaRepository<OTP,Integer> {
	
	Optional<OTP> findByEmail(String email);
	Optional<OTP> findByEmailAndOtp(String email, String otp);
	List<OTP> findByExpiredTimeBefore(LocalDateTime currentTime);
	
	
	
}
