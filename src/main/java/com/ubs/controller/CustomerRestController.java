package com.ubs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;
import com.ubs.entity.CustomerDTO;
import com.ubs.service.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired
	CustomerService customerService;
	
	List<Customer> customers = new ArrayList<Customer>();
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/customers/{page}", method=RequestMethod.GET ) 
	@ResponseBody
	public Page<Customer> getCustomers(@PathVariable("page") int page, @PathParam("firstNameFilter") String firstNameFilter, @PathParam("lastNameFilter") String lastNameFilter){
		return customerService.findByFiltersPaging(firstNameFilter, lastNameFilter, page, 200);
	}
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/addressTypes", method=RequestMethod.GET ) 
	@ResponseBody
	public AddressType[] getAddressTypes(){
		return AddressType.values();
	}
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/customer/{customerId}", method=RequestMethod.GET ) 
	@ResponseBody
	public CustomerDTO getCustomerById(@PathVariable("customerId") Long id){
		return new CustomerDTO(customerService.findById(id));
	}
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/customer", method=RequestMethod.POST ) 
	@ResponseBody
	public Customer saveCustomer(@RequestBody @Valid CustomerDTO customerDTO){
		Customer customer = new Customer(customerDTO);
		return customerService.save(customer);
	}
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/customer", method=RequestMethod.PUT ) 
	@ResponseBody
	public Customer editCustomer(@RequestBody CustomerDTO customerDTO){
		Customer customer = new Customer(customerDTO);
		return customerService.save(customer);
	}
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, path="/customer/{customerId}", method=RequestMethod.DELETE ) 
	@ResponseBody
	public void deleteCustomer(@PathVariable("customerId") Long id){
		customerService.deleteById(id);
	}
	
}
