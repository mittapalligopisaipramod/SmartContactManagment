package com.main.Controller;

import com.main.MailSender.Mail;
import com.main.entity.Contacts;
import com.main.entity.User;
import com.main.model.*;
import com.main.service.EmailServiceImp;
import com.main.service.EmailValidator;
import com.main.service.JwtClaimsExtractor;
import com.main.service.userServiceInfo;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class MyController {
	
	private final EmailServiceImp emailService;
	private final userServiceInfo userServiceInfo;
	
	private final JwtClaimsExtractor claimsExtractor;
	
	public MyController (EmailServiceImp emailService , com.main.service.userServiceInfo userServiceInfo , JwtClaimsExtractor claimsExtractor) {
		this.emailService = emailService;
		this.userServiceInfo = userServiceInfo;
		
		this.claimsExtractor = claimsExtractor;
	}
	@GetMapping("/user")
	public ResponseEntity<User> getUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(user);
	}
	
	
	@PostMapping ("/mail/sendEmail")
	public Integer sendEmailToUser(@RequestBody Mail email){
		System.out.println ("enter into sendEmail method()");
		String recipientEmail = email.getEmail();
		if (EmailValidator.isValid(recipientEmail)) {
			Integer response = emailService.sendMail(recipientEmail);
			return response != null ? response : -1;
		} else {
			
			return -1;
		}
	}
	
	
	//Resending otp to mail
	@PostMapping("/mail/resendOtpAgain")
	public Integer resendOtpAgain(@RequestBody OtpResend resendObj){
		System.out.println ("enter into resendOtpAgain method");
		Integer id= resendObj.getId();
		Integer s = emailService.resendOtpAgainService (id);
		System.out.println ("otp is resent");
		return s;
	}
	
	//otp verification
	@PostMapping("/mail/OtpVerification")
	public String OtpVerification(@RequestBody Verify verify){
		
		Integer id1= verify.getId ();
		String otp=verify.getOtp ();
		String s = emailService.otpVerification (id1 , otp);
		System.out.println ("otp is verified");
		return s;
		
	}
	
	//to register the user for access the rescource
	@PostMapping("/user/toRegister")
	public String toRegister(@RequestBody User user){
		if(user!=null){
			return userServiceInfo.register (user);
		}
		return "try again later";
	}
	@PostMapping("/user/userLogin")
	public ResponseEntity<AuthenticationResponse>UserLogin(@RequestBody UserModel user){
		
		if(user==null){
			return null;
		}
		else if(user.getUser_name ()==null || user.getPassword ()==null){
			return null;
		}
		String username=user.getUser_name ();
		String password=user.getPassword ();
		System.out.println (username + " : " + password);
		ResponseEntity<AuthenticationResponse>object=userServiceInfo.loginProcess(username,password);
		return object;
		
	}
	
	//adding the contact
	@PostMapping("/contact/addContact")
	public contactDTO addContact(@RequestBody ContactModel contact){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		String userName = user.getUsername ();
		contactDTO res=userServiceInfo.addContactToDataBase(userName, contact);
		return res;
	}
	//deleting the contact
	@CrossOrigin(origins = "*")
	//@PostMapping("/contact/deleteContact/")
	@DeleteMapping("/contact/deleteContact/")
	public String deleteContact(@RequestBody Contacts contacts){
		Integer id=contacts.getId ();
		System.out.println ("enter into delete contact method");
		String res= userServiceInfo.deleteContact(Integer.valueOf (id));
		if(res==null){
			return null;
		}
		System.out.println ("contact is deleted())");
		return  "ok";
	}
	@CrossOrigin(origins = "*")
	//@PostMapping("/contact/updateContactNumber/")
	@PutMapping("/contact/updateContactNumber/")
	public contactDTO updateContactNumber(@RequestBody ContactId contact){
		contactDTO object= userServiceInfo.updateContactNumber( contact);
		return object;
	}
	@CrossOrigin(origins = "*")
	@GetMapping("/contact/getAllContacts")
	public List<contactDTO> getAllContacts(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		String username =user.getUsername ();
		System.out.println ("enter into get all methods");
		System.out.println ("user name is " + username);
		if(username==null){
			System.out.println ("username is null");
			return null;
		}
		List<contactDTO> contacts=userServiceInfo.getAllContacts(username);
		System.out.println (contacts.toString ());
		return contacts;
	}
	
	
	@PostMapping("/user/changePassword/sendOtp/toMail")
	public Integer changePasswordVerifyMailAndSendOtp(@RequestBody sendOtpMailForToChangePassword changePassword){
		String mail=changePassword.getMail ();
		System.out.println ("mail sended for changed password");
		System.out.println ("mail is :"+mail);
		if(mail==null)return null;
		else{
			Integer updatedPassword=userServiceInfo.updatePasswordVerifyMailAndSendOtp(mail);
			System.out.println ("updated password  for otp id is  :"+updatedPassword);
			return updatedPassword;
		}
	}
	@PostMapping("/user/changePassword/verifyOtp/viaMail")
	public String changePasswordVerifyOtp(@RequestBody Verify verify){
		Integer id1= verify.getId ();
		String otp=verify.getOtp ();
		String s = emailService.otpVerification (id1 , otp);
		System.out.println ("otp is verified" +":"+s);
		return s;
		
	}
	@PostMapping("/user/changePassword/viaMail")
	public String changePassword(@RequestBody changePassword changePassword){
		String mail=changePassword.getMail ();
		String password=changePassword.getPassword ();
		String s = userServiceInfo.changePassword (mail, password);
		System.out.println ("password is changed");
		return s;
		
	}
	
	@GetMapping("/getFromHttps")
	public String helloHttp(){
		return "hi https!!!!";
	}
	
	
	
	public class GetUserWithAuthenticationPrincipalAnnotationController {
		
		@GetMapping("/user")
		public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
			return "User Details: " + userDetails.getUsername();
		}
	}
	
	
	
	
	
}
