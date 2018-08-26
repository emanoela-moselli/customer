package com.ubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubs.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>  {
	
	
	void deleteByCustomerId(Long customerId);

}
