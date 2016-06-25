package com.puc.pos.lab1.web.rest;

import com.puc.pos.lab1.Lab11App;
import com.puc.pos.lab1.domain.Customer;
import com.puc.pos.lab1.repository.CustomerRepository;
import com.puc.pos.lab1.service.CustomerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Lab11App.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SITE = "AAAAA";
    private static final String UPDATED_SITE = "BBBBB";
    private static final String DEFAULT_CNPJ = "AAAAA";
    private static final String UPDATED_CNPJ = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerService customerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerService", customerService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerRepository.deleteAll();
        customer = new Customer();
        customer.setName(DEFAULT_NAME);
        customer.setSite(DEFAULT_SITE);
        customer.setCnpj(DEFAULT_CNPJ);
        customer.setAddress(DEFAULT_ADDRESS);
    }

    @Test
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testCustomer.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testCustomer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        // Get all the customers
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE.toString())))
                .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.save(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customer.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customer.getId());
        updatedCustomer.setName(UPDATED_NAME);
        updatedCustomer.setSite(UPDATED_SITE);
        updatedCustomer.setCnpj(UPDATED_CNPJ);
        updatedCustomer.setAddress(UPDATED_ADDRESS);

        restCustomerMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testCustomer.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
