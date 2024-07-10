package com.main.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString (exclude = "contacts")
@Entity
public class User  implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	@NotNull(message = "Name cannot be null")
	private String fullName; // first name and last name
	
	@NotNull(message = "username can't be null")
	private String username;
	
	@NotNull(message = "password can't be null")
	private String password;
	
	
	
	public String getFullName () {
		return fullName;
	}
	
	public void setFullName (String fullName) {
		this.fullName = fullName;
	}
	
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length = 100000, nullable = false)
	private String image;
	
	public String getImage () {
		return image;
	}
	
	public void setImage (String image) {
		this.image = image;
	}
	
	public List<Contacts> getContacts () {
		return contacts;
	}
	
	
	
	public void setContacts (List<Contacts> contacts) {
		this.contacts = contacts;
	}
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contacts> contacts; // Bidirectional one-to-many
	
	
	
	public Integer getId () {
		return id;
	}
	
	public void setId (Integer id) {
		this.id = id;
	}
	
	
	
	public String getUsername () {
		return username;
	}
	
	@Override
	public boolean isAccountNonExpired () {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked () {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired () {
		return true;
	}
	
	@Override
	public boolean isEnabled () {
		return true;
	}
	
	public void setUsername (String username) {
		this.username = username;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return List.of (new SimpleGrantedAuthority (role.name ()));
	}
	
	public String getPassword () {
		return password;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public Role getRole () {
		return role;
	}
	
	public void setRole (Role role) {
		this.role = role;
	}
}
