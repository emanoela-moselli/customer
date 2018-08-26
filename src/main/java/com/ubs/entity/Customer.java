package com.ubs.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
	
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	@OneToMany(mappedBy = "customer",fetch=FetchType.LAZY, cascade = {CascadeType.ALL} )
	@MapKey(name = "type")
	@JsonIgnore
	private Map<AddressType, Address> address = new HashMap<AddressType, Address>();
	
	
	public Customer(){
		
	}
	
	public Customer(CustomerDTO customerDto){
		this.id = customerDto.getId();
		this.firstName = customerDto.getFirstName();
		this.lastName = customerDto.getLastName();
		this.phoneNumber = customerDto.getPhoneNumber();
		customerDto.getAddresses().forEach(addressDto->{
			addressDto.setCustomer(this);
			address.put(addressDto.getType(), addressDto);
		});
		
//		for (Address addressDto : customerDto.getAddresses()) {
//			addressDto.setCustomer(this);
//			address.put(addressDto.getType(), addressDto);
//		}
	}
	
	public Customer(String firstName, String lastName, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}

	public Long getId() {
		return id;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void addAddress(Address address){
		address.setCustomer(this);
		this.address.put(address.getType(),address);
	}
	
	public Address getAddressByType(AddressType type){
		return address.get(type);
	}
	
	public Map<AddressType, Address> getAddress() {
		return address;
	}

	public void setAddress(Map<AddressType, Address> address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + "]";
	}
	
	
}
