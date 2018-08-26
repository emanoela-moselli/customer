package com.ubs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.controller.CustomerRestController;
import com.ubs.entity.Address;
import com.ubs.entity.AddressType;
import com.ubs.entity.Customer;
import com.ubs.entity.CustomerDTO;
import com.ubs.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
public class CustomerRestControllerTest {

	private MockMvc mockMvc;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Autowired
	CustomerRestController customerRESTController;
	
	@Autowired
	CustomerService customerService;
	
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(customerRESTController).build();
	}
	
	private Customer createCustomer() {
		Customer customer = new Customer("Emanoela","Moselli","9988998899");
		customer.addAddress(new Address("Street Billing","79","31555-00","Krakow","Poland",AddressType.BILLING));
		customer.addAddress(new Address("Street Shipping","79","31555-00","Krakow","Poland",AddressType.SHIPPING));
		customer.addAddress(new Address("Street Default","79","31555-00","Krakow","Poland",AddressType.DEFAULT));
		
		return customer;
	}

	@Test
	public void testGetCustomers() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/customers/"+0).param("firstNameFilter", "").param("lastNameFilter", "")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testGetAddressTypes() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/addressTypes/")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testGetACustomerById() throws Exception {
		customerService.save(createCustomer());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/customer/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testSaveCustomer() throws Exception {
		Customer customer = createCustomer();
		CustomerDTO customerDTO = new CustomerDTO(customer);
		ObjectMapper customerJson = new ObjectMapper();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/customer")
				.accept(MediaType.APPLICATION_JSON).content(customerJson.writeValueAsString(customerDTO))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testEditCustomer() throws Exception {
		customerService.save(createCustomer());
		Customer customer = customerService.findById(1l);
		CustomerDTO customerDTO = new CustomerDTO(customer);
		ObjectMapper customerJson = new ObjectMapper();

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/customer")
				.accept(MediaType.APPLICATION_JSON).content(customerJson.writeValueAsString(customerDTO))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
//	@Test
	public void testDeleteCustomer() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/customer/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
}
