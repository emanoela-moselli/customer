package com.ubs.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubs.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>  {
	
	@Query(value = "SELECT c FROM Customer c WHERE "
			+ " (:firstName is null or :firstName='' or upper(c.firstName) like :firstName% ) AND"
			+ " (:lastName is null or :lastName='' or upper(c.lastName) like :lastName% ) " )
	Page<Customer> findByFiltersPaging(@Param("firstName") String firstName, @Param("lastName") String lastName,Pageable pageable);
	
	@Query(value = "SELECT c FROM Customer c JOIN FETCH c.address WHERE c.id = :id ")
	Optional<Customer> findById(@Param("id") Long id);

}
