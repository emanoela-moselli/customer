package com.ubs.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;
import com.ubs.exception.RequiredAddressException;
import com.ubs.repository.AddressRepository;
import com.ubs.repository.CustomerRepository;

@Service
public class CustomerService  {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	private EntityManagerFactory emf;
	
	@Transactional
	public Customer save(Customer customer) {
		if(validateRequiredAddresses(customer)){
			addressRepository.deleteByCustomerId(customer.getId());
			return customerRepository.save(customer);
		} else {
			throw new RequiredAddressException(customer);
		}
	}
	 
    @Autowired
    public CustomerService(EntityManagerFactory emf) {
        Assert.notNull(emf, "EntityManagerFactory must not be null");
        this.emf = emf;
    }
 
    public void bulkWithEntityManager(List<Customer> items) {
            EntityManager entityManager = emf.createEntityManager();
            entityManager.getTransaction().begin();
            items.forEach(item -> entityManager.persist(item));
            entityManager.getTransaction().commit();
            entityManager.close();
            
               
 
    }
	
	private boolean validateRequiredAddresses(Customer customer) {
		return AddressType.getRequiredAddressTypes().stream().noneMatch(requiredType -> (customer.getAddressByType(requiredType) == null));
	}

	public Long count(){
		return customerRepository.count();
	}
	
	public Customer findById(Long id){
		return customerRepository.findById(id).get();
	}
	
	public Page<Customer> findByFiltersPaging(String firstName, String lastName, int page, int size){
		return customerRepository.findByFiltersPaging(firstName.toUpperCase(), lastName.toUpperCase(), PageRequest.of(page, size, Sort.by(Direction.ASC, "firstName","lastName")));
	}
	
	public void deleteById(Long id){
		customerRepository.deleteById(id);
	}
	
}
