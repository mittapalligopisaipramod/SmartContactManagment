package com.main.service;

import com.main.entity.Contacts;
import com.main.entity.OTP;
import com.main.entity.Role;
import com.main.entity.User;
import com.main.model.AuthenticationResponse;
import com.main.model.ContactId;
import com.main.model.ContactModel;
import com.main.model.contactDTO;
import com.main.repository.ContactRepo;
import com.main.repository.UserRepository;
import com.main.repository.otpRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class userServiceInfoImp implements userServiceInfo {
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwt;
	private final ContactRepo contactRepo;
	private final AuthenticationManager authenticationManager;
	private final EmailServiceImp emailService;
	private final com.main.repository.otpRepo otpRepo;
	
	public userServiceInfoImp (UserRepository userRepo , PasswordEncoder passwordEncoder , JwtService jwt , JwtClaimsExtractor claims , ContactRepo contactRepo , AuthenticationManager authenticationManager , EmailServiceImp emailService , otpRepo otpRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwt = jwt;
		this.contactRepo = contactRepo;
		this.authenticationManager = authenticationManager;
		
		this.emailService = emailService;
		this.otpRepo = otpRepo;
	}
	
	
	@Override
	public String register (User user) {
		
		user.setRole (Role.valueOf ("USER"));
		System.out.println (user.toString ());
		user.setPassword (passwordEncoder.encode (user.getPassword ()));
		User saved = userRepo.save (user);
		System.out.println (saved);
		return "registered done";
			
		
		
		
	}
	public ResponseEntity<AuthenticationResponse> loginProcess(String username, String password) {
		try {
			
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken (username, password)
			);
			
			Optional<User> user = userRepo.findByUsername (username);
			if(user.isEmpty()){
				 //return ResponseEntity.status (HttpStatus.NOT_FOUND).body(null);
				System.out.println ("the user is not there bro");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthenticationResponse("", "", ""));
				
			}
			
			String fullName=user.get ().getFullName ();
			String image=user.get ().getImage ();
			 
		 
			 // Generate the JWT token
			String token = jwt.generateToken(username);
			
			
			//returning the jwt token and fullname and image persisted in database
			AuthenticationResponse response = new AuthenticationResponse(token, fullName,image);
			
			
			return ResponseEntity.ok(response);
			
		} catch (AuthenticationException e) {
			System.out.println ("exception occured: " +e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("", "", ""));
			//return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@Transactional
	@Override
	public contactDTO addContactToDataBase (String username , ContactModel contactFromUser) {
		
		Optional<User> user= userRepo.findByUsername (username);
		if(user.isEmpty ()) {
			return null;
		}
		Contacts contacts= Contacts.builder()
				.contactName (contactFromUser.getContactName())
				.contactNumber (contactFromUser.getContactNumber ())
				.user(user.get ())
				.build();
		Contacts contacts1 = contactRepo.save (contacts);
		Integer c_id=contacts1.getId ();
		Contacts contacts2 = contactRepo.findById (c_id).get ();
		contactDTO contactNewAdded=contactDTO.builder ()
				.id (contacts2.getId ())
				.contactName (contacts2.getContactName ())
				.contactNumber (contacts2.getContactNumber ())
				.build ();
		return contactNewAdded;
		
	}
	
	
	@Override
	public String deleteContact ( Integer id) {
		
		Optional<Contacts> contact= contactRepo.findById (id);
		if(contact.isPresent ()) {
			contactRepo.deleteById (id);
			return "contact is deleted";
		}
		return null;
	}
	
	@Override
	public contactDTO updateContactNumber (ContactId contact) {
			Integer id=contact.getId ();
			Optional<Contacts> contact1= contactRepo.findById (id);
			if(contact1.isEmpty ()) {
				return null;
			}
			Contacts contacts=contactRepo.findById (id).get ();
			contacts.setContactName (contact.getContactName ());
			contacts.setContactNumber (BigDecimal.valueOf (contact.getContactNumber ()));
			Contacts saved = contactRepo.save (contacts);
			contactDTO contactUpdated=contactDTO.builder ().build ();
			contactUpdated.setId (saved.getId ());
			contactUpdated.setContactName (saved.getContactName ());
			contactUpdated.setContactNumber (saved.getContactNumber ());
			System.out.println (contactUpdated.toString ());
			return  contactUpdated;
	}
	@Override
	public List<contactDTO> getAllContacts (String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		List<Contacts> contacts = contactRepo.findByUser(user);
		if(contacts.isEmpty ()) {
			System.out.println ("contact are emypy ");
		 //	List.of (contactDTO.builder ().contactName ("demo").contactNumber (BigDecimal.valueOf (234324)).id (1).build ());
			return List.of ();
		}
		List<contactDTO> contactDTOS = convertToDTO (contacts);
		return contactDTOS;
	}
	
	@Override
	public Integer updatePasswordVerifyMailAndSendOtp (String mail) {
		Optional<User> user = userRepo.findByUsername (mail);
		if (user.isPresent ()) {
			
			Optional<OTP> byEmail = otpRepo.findByEmail (mail);
			if (byEmail.isPresent ()) {
				Integer id = byEmail.get ().getId ();
				System.out.println ("id for otp is determined " + id);
				Integer i = emailService.resendOtpAgainService (id);
				System.out.println (i);
				return i;
				
			}
		}
		return null;
	}
	
	@Override
	public String changePassword (String mail , String password) {
		Optional<User> user = userRepo.findByUsername (mail);
		if(user.isEmpty ()){
			return null;
		}else{
			user.get ().setPassword (passwordEncoder.encode (password));
			userRepo.save (user.get ());
			return "ok";
		}
		
	}
	
	public List<contactDTO> convertToDTO(List<Contacts> contacts) {
		return contacts.stream().map(contact -> contactDTO.builder()
				.id(contact.getId())
				.contactName(contact.getContactName())
				.contactNumber(contact.getContactNumber())
				.build()).collect(Collectors.toList());
	}
}
