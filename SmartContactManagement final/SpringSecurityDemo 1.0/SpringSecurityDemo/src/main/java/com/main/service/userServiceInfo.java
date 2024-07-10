package com.main.service;


import com.main.entity.User;
import com.main.model.AuthenticationResponse;
import com.main.model.ContactId;
import com.main.model.ContactModel;
import com.main.model.contactDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface userServiceInfo {
	
	String register (User user);
	
	ResponseEntity<AuthenticationResponse> loginProcess (String username , String password);
	
	contactDTO addContactToDataBase (String userName , ContactModel contact);
	
	String deleteContact (Integer integer);
	
	
	contactDTO updateContactNumber (ContactId contact);
	
	List<contactDTO> getAllContacts (String username);
	
	
	Integer updatePasswordVerifyMailAndSendOtp (String mail);
	
	String changePassword (String mail , String password);
}
