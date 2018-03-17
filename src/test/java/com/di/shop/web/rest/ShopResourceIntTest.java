package com.di.shop.web.rest;

import com.di.shop.SampleApp1App;

import com.di.shop.domain.Shop;
import com.di.shop.repository.ShopRepository;
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
import java.util.List;

import static com.di.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ShopResource REST controller.
 *
 * @see ShopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApp1App.class)
public class ShopResourceIntTest {

    private static final Long DEFAULT_S_ID = 1L;
    private static final Long UPDATED_S_ID = 2L;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopMockMvc;

    private Shop shop;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopResource shopResource = new ShopResource(shopRepository);
        this.restShopMockMvc = MockMvcBuilders.standaloneSetup(shopResource)
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
    public static Shop createEntity(EntityManager em) {
        Shop shop = new Shop()
            .sId(DEFAULT_S_ID)
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME);
        return shop;
    }

    @Before
    public void initTest() {
        shop = createEntity(em);
    }

    @Test
    @Transactional
    public void createShop() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate + 1);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getsId()).isEqualTo(DEFAULT_S_ID);
        assertThat(testShop.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testShop.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testShop.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createShopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopRepository.findAll().size();

        // Create the Shop with an existing ID
        shop.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isBadRequest());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setLogin(null);

        // Create the Shop, which fails.

        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = shopRepository.findAll().size();
        // set the field null
        shop.setPassword(null);

        // Create the Shop, which fails.

        restShopMockMvc.perform(post("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isBadRequest());

        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShops() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get all the shopList
        restShopMockMvc.perform(get("/api/shops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().intValue())))
            .andExpect(jsonPath("$.[*].sId").value(hasItem(DEFAULT_S_ID.intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);

        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", shop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shop.getId().intValue()))
            .andExpect(jsonPath("$.sId").value(DEFAULT_S_ID.intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShop() throws Exception {
        // Get the shop
        restShopMockMvc.perform(get("/api/shops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);
        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Update the shop
        Shop updatedShop = shopRepository.findOne(shop.getId());
        // Disconnect from session so that the updates on updatedShop are not directly saved in db
        em.detach(updatedShop);
        updatedShop
            .sId(UPDATED_S_ID)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME);

        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShop)))
            .andExpect(status().isOk());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate);
        Shop testShop = shopList.get(shopList.size() - 1);
        assertThat(testShop.getsId()).isEqualTo(UPDATED_S_ID);
        assertThat(testShop.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testShop.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testShop.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingShop() throws Exception {
        int databaseSizeBeforeUpdate = shopRepository.findAll().size();

        // Create the Shop

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShopMockMvc.perform(put("/api/shops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shop)))
            .andExpect(status().isCreated());

        // Validate the Shop in the database
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShop() throws Exception {
        // Initialize the database
        shopRepository.saveAndFlush(shop);
        int databaseSizeBeforeDelete = shopRepository.findAll().size();

        // Get the shop
        restShopMockMvc.perform(delete("/api/shops/{id}", shop.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Shop> shopList = shopRepository.findAll();
        assertThat(shopList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shop.class);
        Shop shop1 = new Shop();
        shop1.setId(1L);
        Shop shop2 = new Shop();
        shop2.setId(shop1.getId());
        assertThat(shop1).isEqualTo(shop2);
        shop2.setId(2L);
        assertThat(shop1).isNotEqualTo(shop2);
        shop1.setId(null);
        assertThat(shop1).isNotEqualTo(shop2);
    }
}
