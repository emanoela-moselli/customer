package com.ubs.util;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import com.ubs.entity.Address;
import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;

public class CustomerSqlCreator {

	 
	 
	  public static void main(String[] args) throws IOException {
	    Scanner ler = new Scanner(System.in);
	 
	    FileWriter customerFile = new FileWriter("//Users//emanoelamoselli///Documents//workspace//ubs//src//main//resources//data.sql");
	    PrintWriter customerSqlFile = new PrintWriter(customerFile);
	    
	    FileWriter billingFile = new FileWriter("//Users//emanoelamoselli///Documents//workspace//ubs//src//main//resources//billing.sql");
	    PrintWriter billingSqlFile = new PrintWriter(billingFile);
	    
	    FileWriter shippingFile = new FileWriter("//Users//emanoelamoselli///Documents//workspace//ubs//src//main//resources//shipping.sql");
	    PrintWriter shippingSqlFile = new PrintWriter(shippingFile);
	    
	    FileWriter defaultFile = new FileWriter("//Users//emanoelamoselli///Documents//workspace//ubs//src//main//resources//default.sql");
	    PrintWriter defaultSqlFile = new PrintWriter(defaultFile);
	 
	    String[] names = {"Emanoela","Anna","Maria","Vinicius","Patrick","Ronald","Peter","Emilia","Raphael"};
		String[] lastName = {"Moselli","Cunha","Neves","Zeola"};
		String[] city = {"Krakow","Warsaw","Lodz","Wroclaw","Poznan","Gdansk","Zakopane","Lublin","Katowice","Zator"};
		Random random = new Random();
		int contadorAddress = 0;
		
		for (int i = 1; i < 500000; i++) {
			Customer customer = new Customer("Emanoela"+ i,lastName[random.nextInt(4)]+i,String.valueOf(random.nextInt(999999999)));
			customer.addAddress(new Address("Street Billing"+i,""+i,String.valueOf(random.nextInt(9999999)),city[random.nextInt(10)],"Poland",AddressType.BILLING));
			customer.addAddress(new Address("Street Shipping"+i,""+i,String.valueOf(random.nextInt(9999999)),city[random.nextInt(10)],"Poland",AddressType.SHIPPING));
			customer.addAddress(new Address("Street Default"+i,""+i,String.valueOf(random.nextInt(9999999)),city[random.nextInt(10)],"Poland",AddressType.DEFAULT));
			
			customerSqlFile.printf("INSERT INTO customer (id, first_name, last_name, phone_number) values ("+i+",'"+customer.getFirstName()+"','"+customer.getLastName()+"','"+customer.getPhoneNumber()+"');%n");
			
			contadorAddress++;
			billingSqlFile.printf("INSERT INTO address (id,address, number,zip_code,city,country,type,customer_id) values ("+contadorAddress+",'Street Billing"+i+"','"+i+"','"+String.valueOf(random.nextInt(9999999))+"','"+city[random.nextInt(10)]+"','Poland','BILLING',"+i+");%n");
			contadorAddress++;
			shippingSqlFile.printf("INSERT INTO address (id,address, number,zip_code,city,country,type,customer_id) values ("+contadorAddress+",'Street Shipping"+i+"','"+i+"','"+String.valueOf(random.nextInt(9999999))+"','"+city[random.nextInt(10)]+"','Poland','SHIPPING',"+i+");%n");
			contadorAddress++;
			defaultSqlFile.printf("INSERT INTO address (id,address, number,zip_code,city,country,type,customer_id) values ("+contadorAddress+",'Street Default"+i+"','"+i+"','"+String.valueOf(random.nextInt(9999999))+"','"+city[random.nextInt(10)]+"','Poland','DEFAULT',"+i+");%n");
			
		}
		
	    
	 
		customerFile.close();
		billingFile.close();
		shippingFile.close();
		defaultFile.close();
	 
	  }
	 
}
