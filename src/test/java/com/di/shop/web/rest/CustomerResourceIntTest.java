package com.di.shop.web.rest;

import com.di.shop.SampleApp1App;

import com.di.shop.domain.Customer;
import com.di.shop.repository.CustomerRepository;
import com.di.shop.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.di.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApp1App.class)
public class CustomerResourceIntTest {

    private static final Long DEFAULT_U_ID = 1L;
    private static final Long UPDATED_U_ID = 2L;

    private static final String DEFAULT_U_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_U_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_U_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_U_PASSWORD = "BBBBBBBBBB";

    private static final Long DEFAULT_U_TOTAL_CREDITS = 1L;
    private static final Long UPDATED_U_TOTAL_CREDITS = 2L;

    private static final Long DEFAULT_U_PLACED_CREDITS = 1L;
    private static final Long UPDATED_U_PLACED_CREDITS = 2L;

    private static final Long DEFAULT_U_GAINED_CREDITS = 1L;
    private static final Long UPDATED_U_GAINED_CREDITS = 2L;

    private static final String DEFAULT_U_NAME = "AAAAAAAAAA";
    private static final String UPDATED_U_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_U_CONTACT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_U_CONTACT_INFO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerRepository);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .uId(DEFAULT_U_ID)
            .uLogin(DEFAULT_U_LOGIN)
            .uPassword(DEFAULT_U_PASSWORD)
            .uTotalCredits(DEFAULT_U_TOTAL_CREDITS)
            .uPlacedCredits(DEFAULT_U_PLACED_CREDITS)
            .uGainedCredits(DEFAULT_U_GAINED_CREDITS)
            .uName(DEFAULT_U_NAME)
            .uContactInfo(DEFAULT_U_CONTACT_INFO)
            .createdOn(DEFAULT_CREATED_ON);
        return customer;
    }

    @Before
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getuId()).isEqualTo(DEFAULT_U_ID);
        assertThat(testCustomer.getuLogin()).isEqualTo(DEFAULT_U_LOGIN);
        assertThat(testCustomer.getuPassword()).isEqualTo(DEFAULT_U_PASSWORD);
        assertThat(testCustomer.getuTotalCredits()).isEqualTo(DEFAULT_U_TOTAL_CREDITS);
        assertThat(testCustomer.getuPlacedCredits()).isEqualTo(DEFAULT_U_PLACED_CREDITS);
        assertThat(testCustomer.getuGainedCredits()).isEqualTo(DEFAULT_U_GAINED_CREDITS);
        assertThat(testCustomer.getuName()).isEqualTo(DEFAULT_U_NAME);
        assertThat(testCustomer.getuContactInfo()).isEqualTo(DEFAULT_U_CONTACT_INFO);
        assertThat(testCustomer.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkuLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setuLogin(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkuPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setuPassword(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkuTotalCreditsIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setuTotalCredits(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].uId").value(hasItem(DEFAULT_U_ID.intValue())))
            .andExpect(jsonPath("$.[*].uLogin").value(hasItem(DEFAULT_U_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].uPassword").value(hasItem(DEFAULT_U_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].uTotalCredits").value(hasItem(DEFAULT_U_TOTAL_CREDITS.intValue())))
            .andExpect(jsonPath("$.[*].uPlacedCredits").value(hasItem(DEFAULT_U_PLACED_CREDITS.intValue())))
            .andExpect(jsonPath("$.[*].uGainedCredits").value(hasItem(DEFAULT_U_GAINED_CREDITS.intValue())))
            .andExpect(jsonPath("$.[*].uName").value(hasItem(DEFAULT_U_NAME.toString())))
            .andExpect(jsonPath("$.[*].uContactInfo").value(hasItem(DEFAULT_U_CONTACT_INFO.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.uId").value(DEFAULT_U_ID.intValue()))
            .andExpect(jsonPath("$.uLogin").value(DEFAULT_U_LOGIN.toString()))
            .andExpect(jsonPath("$.uPassword").value(DEFAULT_U_PASSWORD.toString()))
            .andExpect(jsonPath("$.uTotalCredits").value(DEFAULT_U_TOTAL_CREDITS.intValue()))
            .andExpect(jsonPath("$.uPlacedCredits").value(DEFAULT_U_PLACED_CREDITS.intValue()))
            .andExpect(jsonPath("$.uGainedCredits").value(DEFAULT_U_GAINED_CREDITS.intValue()))
            .andExpect(jsonPath("$.uName").value(DEFAULT_U_NAME.toString()))
            .andExpect(jsonPath("$.uContactInfo").value(DEFAULT_U_CONTACT_INFO.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findOne(customer.getId());
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .uId(UPDATED_U_ID)
            .uLogin(UPDATED_U_LOGIN)
            .uPassword(UPDATED_U_PASSWORD)
            .uTotalCredits(UPDATED_U_TOTAL_CREDITS)
            .uPlacedCredits(UPDATED_U_PLACED_CREDITS)
            .uGainedCredits(UPDATED_U_GAINED_CREDITS)
            .uName(UPDATED_U_NAME)
            .uContactInfo(UPDATED_U_CONTACT_INFO)
            .createdOn(UPDATED_CREATED_ON);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getuId()).isEqualTo(UPDATED_U_ID);
        assertThat(testCustomer.getuLogin()).isEqualTo(UPDATED_U_LOGIN);
        assertThat(testCustomer.getuPassword()).isEqualTo(UPDATED_U_PASSWORD);
        assertThat(testCustomer.getuTotalCredits()).isEqualTo(UPDATED_U_TOTAL_CREDITS);
        assertThat(testCustomer.getuPlacedCredits()).isEqualTo(UPDATED_U_PLACED_CREDITS);
        assertThat(testCustomer.getuGainedCredits()).isEqualTo(UPDATED_U_GAINED_CREDITS);
        assertThat(testCustomer.getuName()).isEqualTo(UPDATED_U_NAME);
        assertThat(testCustomer.getuContactInfo()).isEqualTo(UPDATED_U_CONTACT_INFO);
        assertThat(testCustomer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
