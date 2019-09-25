package com.innvo.web.rest;

import com.innvo.JhipsterApp;
import com.innvo.domain.Parent;
import com.innvo.repository.ParentRepository;
import com.innvo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.innvo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParentResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class ParentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restParentMockMvc;

    private Parent parent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParentResource parentResource = new ParentResource(parentRepository);
        this.restParentMockMvc = MockMvcBuilders.standaloneSetup(parentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parent createEntity(EntityManager em) {
        Parent parent = new Parent()
            .name(DEFAULT_NAME);
        return parent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parent createUpdatedEntity(EntityManager em) {
        Parent parent = new Parent()
            .name(UPDATED_NAME);
        return parent;
    }

    @BeforeEach
    public void initTest() {
        parent = createEntity(em);
    }

    @Test
    @Transactional
    public void createParent() throws Exception {
        int databaseSizeBeforeCreate = parentRepository.findAll().size();

        // Create the Parent
        restParentMockMvc.perform(post("/api/parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isCreated());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeCreate + 1);
        Parent testParent = parentList.get(parentList.size() - 1);
        assertThat(testParent.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createParentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parentRepository.findAll().size();

        // Create the Parent with an existing ID
        parent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParentMockMvc.perform(post("/api/parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isBadRequest());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentRepository.findAll().size();
        // set the field null
        parent.setName(null);

        // Create the Parent, which fails.

        restParentMockMvc.perform(post("/api/parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isBadRequest());

        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParents() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList
        restParentMockMvc.perform(get("/api/parents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", parent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParent() throws Exception {
        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        int databaseSizeBeforeUpdate = parentRepository.findAll().size();

        // Update the parent
        Parent updatedParent = parentRepository.findById(parent.getId()).get();
        // Disconnect from session so that the updates on updatedParent are not directly saved in db
        em.detach(updatedParent);
        updatedParent
            .name(UPDATED_NAME);

        restParentMockMvc.perform(put("/api/parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParent)))
            .andExpect(status().isOk());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeUpdate);
        Parent testParent = parentList.get(parentList.size() - 1);
        assertThat(testParent.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingParent() throws Exception {
        int databaseSizeBeforeUpdate = parentRepository.findAll().size();

        // Create the Parent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentMockMvc.perform(put("/api/parents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isBadRequest());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        int databaseSizeBeforeDelete = parentRepository.findAll().size();

        // Delete the parent
        restParentMockMvc.perform(delete("/api/parents/{id}", parent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parent.class);
        Parent parent1 = new Parent();
        parent1.setId(1L);
        Parent parent2 = new Parent();
        parent2.setId(parent1.getId());
        assertThat(parent1).isEqualTo(parent2);
        parent2.setId(2L);
        assertThat(parent1).isNotEqualTo(parent2);
        parent1.setId(null);
        assertThat(parent1).isNotEqualTo(parent2);
    }
}
