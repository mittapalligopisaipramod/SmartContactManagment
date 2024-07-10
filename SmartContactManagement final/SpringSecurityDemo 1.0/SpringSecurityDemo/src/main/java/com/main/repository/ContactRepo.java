package com.main.repository;


import com.main.entity.Contacts;
import com.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepo extends JpaRepository<Contacts,Integer> {
	
	
	
	Optional<Contacts>findByContactNumber(Integer contactNumber);
	List<Contacts> findByUser (User user);
	List<Contacts> findByUserId(Integer userId);
	
}
