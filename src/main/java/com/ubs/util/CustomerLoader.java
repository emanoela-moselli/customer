package com.ubs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ubs.entity.Address;
import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;
import com.ubs.service.CustomerService;

@Component
public class CustomerLoader implements ApplicationRunner {

	@Autowired
	CustomerService customerService;

	@Value("${app.test}")
	private boolean test;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String[] names = {"Emanoela","Anna","Maria","Vinicius","Patrick","Ronald","Peter","Emilia","Raphael"};
		String[] lastName = {"Moselli","Cunha","Neves","Zeola"};
		String[] city = {"Krakow","Warsaw","Lodz","Wroclaw","Poznan","Gdansk","Zakopane","Lublin","Katowice","Zator"};
		Random random = new Random();
		String str4 = "9999999";
		String country = "Poland";
		String _99 = "99";
		String string = "Street Billing";
		String string2 = "Street Shipping";
		String string3 = "Street Default";
		List<Customer> customers = new ArrayList<>();

		if(!test){
			String str = "";
			for (int i = 0; i < 500000; i++) {
				str = String.valueOf(i);
				Customer customer = new Customer(new StringBuffer(names[random.nextInt(7)]).append(str).toString(), lastName[random.nextInt(3)].toString(), new StringBuffer(str).append(str4).toString());

				customer.addAddress(new Address(new StringBuffer(string).append(str).toString(),str,new StringBuffer(str).append(_99).toString(),city[random.nextInt(9)],country,AddressType.BILLING));
				customer.addAddress(new Address(new StringBuffer(string2).append(str).toString(),str,new StringBuffer(str).append(_99).toString(),city[random.nextInt(9)],country,AddressType.SHIPPING));
				customer.addAddress(new Address(new StringBuffer(string3).append(str).toString(),str,new StringBuffer(str).append(_99).toString(),city[random.nextInt(9)],country,AddressType.DEFAULT));
				customers.add(customer);
				if(i % 100 == 0 ){
					customerService.bulkWithEntityManager(customers);
					customers.clear();
				}
				if(i% 100000 == 0){
					System.gc();
				}
			}
		}
	}

}
