package com.ubs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ubs.entity.Address;
import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;
import com.ubs.exception.RequiredAddressException;
import com.ubs.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
public class CustomerTest {

	@Autowired
	CustomerService customerService;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	
	@Test
	public void saveCustomerWithAddress() {
		
		Customer customer = new Customer("Emanoela","Moselli","9988998899");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		Customer customerSaved = customerService.save(customer);	
		
		assertTrue(customerService.count() > 0);
		assertNotNull(customerSaved.getAddressByType(AddressType.BILLING).getId());
		assertNotNull(customerSaved.getAddressByType(AddressType.SHIPPING).getId());
		assertNotNull(customerSaved.getAddressByType(AddressType.DEFAULT).getId());
	}
	
	@Test
	public void findCustomerByIdWithAddress() {
		Customer customer = new Customer("Emanoela","Moselli","9988998899");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		
		customer = customerService.save(customer);
		Customer customerRecovered = customerService.findById(customer.getId());
		
		assertNotNull(customerRecovered.getId());
		assertNotNull(customerRecovered.getAddressByType(AddressType.BILLING).getId());
		assertNotNull(customerRecovered.getAddressByType(AddressType.SHIPPING).getId());
		assertNotNull(customerRecovered.getAddressByType(AddressType.DEFAULT).getId());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void findCustomerWithInvalidId(){
		customerService.findById(999999999L);
	}
	
	@Test
	public void saveCustomerWithoutRequiredAddress(){
		Customer customer = new Customer("Emanoela","Moselli","9988998899");
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		
		exception.expect(RequiredAddressException.class);
		customer = customerService.save(customer);
	}
	
	
	@Test
	public void editCustomer(){
		Customer customer = new Customer("Emanoela","Moselli","884665712");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		
		customer = customerService.save(customer);
		Customer customerRecovered = customerService.findById(customer.getId());
		
		customerRecovered.setFirstName("Anna");
		customerRecovered.setLastName("Neves");
		customerRecovered.setPhoneNumber("884665713");
		customerRecovered = customerService.save(customerRecovered);
		
		Customer customerUpdated = customerService.findById(customerRecovered.getId());
		assertEquals("Anna", customerUpdated.getFirstName());
		assertEquals("Neves", customerUpdated.getLastName());
		assertEquals("884665713", customerUpdated.getPhoneNumber());
	}
	
	@Test
	public void editAddressFromCustomer(){
		Customer customer = new Customer("Emanoela","Moselli","884665712");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		
		customer = customerService.save(customer);
		Customer customerRecovered = customerService.findById(customer.getId());
		
		customerRecovered.getAddressByType(AddressType.BILLING).setCity("Sao Paulo");
		customerRecovered.getAddressByType(AddressType.BILLING).setCountry("Brazil");
		customerRecovered = customerService.save(customerRecovered);
		
		Customer customerUpdated = customerService.findById(customerRecovered.getId());
		assertEquals("Sao Paulo",customerUpdated.getAddressByType(AddressType.BILLING).getCity());
		assertEquals("Brazil", customerUpdated.getAddressByType(AddressType.BILLING).getCountry());
	}
	
	@Test
	public void editCustomerAndAddress(){
		Customer customer = new Customer("Emanoela","Moselli","884665712");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		customer = customerService.save(customer);
		
		Customer customerRecovered = customerService.findById(customer.getId());
		customerRecovered.getAddressByType(AddressType.BILLING).setCity("Sao Paulo");
		customerRecovered.setLastName("After Edit");
		customerRecovered = customerService.save(customerRecovered);
		
		Customer customerUpdated = customerService.findById(customerRecovered.getId());
		
		assertEquals("Sao Paulo",customerUpdated.getAddressByType(AddressType.BILLING).getCity());
		assertEquals("After Edit",customerUpdated.getLastName());
	}
	
	@Test
	public void findSomeCustomers(){
		
		for (int i = 0; i < 10; i++) {
			Customer customer = new Customer("Maria"+i,"Moselli"+i,"88466571"+i);
			customer.addAddress(new Address("Street Billing"+i,""+i,"31555-00","Krakow","Poland",AddressType.BILLING));
			customer.addAddress(new Address("Street Shipping",""+i,"31555-00","Krakow","Poland",AddressType.SHIPPING));
			customer.addAddress(new Address("Street Default",""+i,"31555-00","Krakow","Poland",AddressType.DEFAULT));
			customer = customerService.save(customer);
		}
		
		List<Customer> customers = customerService.findByFiltersPaging("","",0,3).getContent();
		
		assertEquals(3, customers.size());
	}
	
	@Test
	public void filterCustomersByLastName(){
		List<Customer> customers = customerService.findByFiltersPaging("","Mose",0,100).getContent();
		
		boolean lastNameStartsWith = true;
		for (Customer customer : customers) {
			if(!customer.getLastName().startsWith("Mose")){
				lastNameStartsWith = false;
			}
		}
		
		assertTrue(lastNameStartsWith);
	}
	
	@Test
	public void filterCustomersByFirstName(){
		List<Customer> customers = customerService.findByFiltersPaging("Ema","",0,100).getContent();
		
		boolean firstNameStartsWith = true;
		for (Customer customer : customers) {
			if(!customer.getFirstName().startsWith("Eman")){
				firstNameStartsWith = false;
			}
		}
		
		assertTrue(firstNameStartsWith);
	}
	
	@Test
	public void filterCustomersByFirstAndLastNames(){
		List<Customer> customers = customerService.findByFiltersPaging("Ema","Mose",0,100).getContent();
		
		boolean filterStartsWith = true;
		for (Customer customer : customers) {
			if(!customer.getFirstName().startsWith("Eman") || !customer.getLastName().startsWith("Mose")){
				filterStartsWith = false;
			}
		}
		
		assertTrue(filterStartsWith);
	}
	
	@Test
	public void removeCustomerById(){
		Customer customerRecovered = customerService.findById(1l);
		customerService.deleteById(customerRecovered.getId());
		exception.expect(NoSuchElementException.class);
		customerService.findById(1l);
	}
	
}
