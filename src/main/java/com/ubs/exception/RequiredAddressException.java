package com.ubs.exception;

import com.ubs.entity.Customer;

public class RequiredAddressException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String REQUIRED_ADDRESS = "Missing required address";
	 
    public RequiredAddressException(Customer customer) {
        super(String.format(REQUIRED_ADDRESS, customer.toString()));
         
    }
}
