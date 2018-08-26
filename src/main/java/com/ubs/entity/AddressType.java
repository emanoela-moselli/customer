package com.ubs.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AddressType {
	
	BILLING(true),
	SHIPPING(true),
	DEFAULT(true);
	
	private boolean required;
	
	AddressType ( boolean required){
		this.required = required;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public static List<AddressType> getRequiredAddressTypes(){
		List<AddressType> requiredsAddressTypes = new ArrayList<>();
		List<AddressType> addresses = new ArrayList<AddressType>(Arrays.asList(AddressType.values()));
		addresses.forEach(addressType->{
			if(addressType.required){
				requiredsAddressTypes.add(addressType);
			}
		});
		
		return requiredsAddressTypes;
	}
}
