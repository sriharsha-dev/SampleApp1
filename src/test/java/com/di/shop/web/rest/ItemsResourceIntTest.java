package com.di.shop.web.rest;

import com.di.shop.SampleApp1App;

import com.di.shop.domain.Items;
import com.di.shop.repository.ItemsRepository;
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
 * Test class for the ItemsResource REST controller.
 *
 * @see ItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApp1App.class)
public class ItemsResourceIntTest {

    private static final Long DEFAULT_ITEM_ID = 1L;
    private static final Long UPDATED_ITEM_ID = 2L;

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_TOTAL_PLACED_CREDITS = 1L;
    private static final Long UPDATED_TOTAL_PLACED_CREDITS = 2L;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Double DEFAULT_COMMISSION = 1D;
    private static final Double UPDATED_COMMISSION = 2D;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemsMockMvc;

    private Items items;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemsResource itemsResource = new ItemsResource(itemsRepository);
        this.restItemsMockMvc = MockMvcBuilders.standaloneSetup(itemsResource)
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
    public static Items createEntity(EntityManager em) {
        Items items = new Items()
            .itemId(DEFAULT_ITEM_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .name(DEFAULT_NAME)
            .createdOn(DEFAULT_CREATED_ON)
            .totalPlacedCredits(DEFAULT_TOTAL_PLACED_CREDITS)
            .status(DEFAULT_STATUS)
            .commission(DEFAULT_COMMISSION);
        return items;
    }

    @Before
    public void initTest() {
        items = createEntity(em);
    }

    @Test
    @Transactional
    public void createItems() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isCreated());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate + 1);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemId()).isEqualTo(DEFAULT_ITEM_ID);
        assertThat(testItems.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testItems.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItems.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testItems.getTotalPlacedCredits()).isEqualTo(DEFAULT_TOTAL_PLACED_CREDITS);
        assertThat(testItems.isStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testItems.getCommission()).isEqualTo(DEFAULT_COMMISSION);
    }

    @Test
    @Transactional
    public void createItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemsRepository.findAll().size();

        // Create the Items with an existing ID
        items.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setCreatedBy(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setName(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setCreatedOn(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemsRepository.findAll().size();
        // set the field null
        items.setStatus(null);

        // Create the Items, which fails.

        restItemsMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isBadRequest());

        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get all the itemsList
        restItemsMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(items.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemId").value(hasItem(DEFAULT_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].totalPlacedCredits").value(hasItem(DEFAULT_TOTAL_PLACED_CREDITS.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].commission").value(hasItem(DEFAULT_COMMISSION.doubleValue())));
    }

    @Test
    @Transactional
    public void getItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);

        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", items.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(items.getId().intValue()))
            .andExpect(jsonPath("$.itemId").value(DEFAULT_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.totalPlacedCredits").value(DEFAULT_TOTAL_PLACED_CREDITS.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.commission").value(DEFAULT_COMMISSION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItems() throws Exception {
        // Get the items
        restItemsMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Update the items
        Items updatedItems = itemsRepository.findOne(items.getId());
        // Disconnect from session so that the updates on updatedItems are not directly saved in db
        em.detach(updatedItems);
        updatedItems
            .itemId(UPDATED_ITEM_ID)
            .createdBy(UPDATED_CREATED_BY)
            .name(UPDATED_NAME)
            .createdOn(UPDATED_CREATED_ON)
            .totalPlacedCredits(UPDATED_TOTAL_PLACED_CREDITS)
            .status(UPDATED_STATUS)
            .commission(UPDATED_COMMISSION);

        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItems)))
            .andExpect(status().isOk());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate);
        Items testItems = itemsList.get(itemsList.size() - 1);
        assertThat(testItems.getItemId()).isEqualTo(UPDATED_ITEM_ID);
        assertThat(testItems.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testItems.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItems.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testItems.getTotalPlacedCredits()).isEqualTo(UPDATED_TOTAL_PLACED_CREDITS);
        assertThat(testItems.isStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testItems.getCommission()).isEqualTo(UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    public void updateNonExistingItems() throws Exception {
        int databaseSizeBeforeUpdate = itemsRepository.findAll().size();

        // Create the Items

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItemsMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(items)))
            .andExpect(status().isCreated());

        // Validate the Items in the database
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItems() throws Exception {
        // Initialize the database
        itemsRepository.saveAndFlush(items);
        int databaseSizeBeforeDelete = itemsRepository.findAll().size();

        // Get the items
        restItemsMockMvc.perform(delete("/api/items/{id}", items.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Items> itemsList = itemsRepository.findAll();
        assertThat(itemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Items.class);
        Items items1 = new Items();
        items1.setId(1L);
        Items items2 = new Items();
        items2.setId(items1.getId());
        assertThat(items1).isEqualTo(items2);
        items2.setId(2L);
        assertThat(items1).isNotEqualTo(items2);
        items1.setId(null);
        assertThat(items1).isNotEqualTo(items2);
    }
}
