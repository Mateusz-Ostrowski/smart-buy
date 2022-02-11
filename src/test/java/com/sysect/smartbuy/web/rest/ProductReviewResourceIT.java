package com.sysect.smartbuy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sysect.smartbuy.IntegrationTest;
import com.sysect.smartbuy.domain.ProductReview;
import com.sysect.smartbuy.repository.ProductReviewRepository;
import com.sysect.smartbuy.service.dto.ProductReviewDTO;
import com.sysect.smartbuy.service.mapper.ProductReviewMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductReviewResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/product-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductReviewMapper productReviewMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReviewMockMvc;

    private ProductReview productReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createEntity(EntityManager em) {
        ProductReview productReview = new ProductReview()
            .text(DEFAULT_TEXT)
            .rating(DEFAULT_RATING)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return productReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createUpdatedEntity(EntityManager em) {
        ProductReview productReview = new ProductReview()
            .text(UPDATED_TEXT)
            .rating(UPDATED_RATING)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return productReview;
    }

    @BeforeEach
    public void initTest() {
        productReview = createEntity(em);
    }

    @Test
    @Transactional
    void createProductReview() throws Exception {
        int databaseSizeBeforeCreate = productReviewRepository.findAll().size();
        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);
        restProductReviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeCreate + 1);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testProductReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProductReview.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProductReview.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createProductReviewWithExistingId() throws Exception {
        // Create the ProductReview with an existing ID
        productReview.setId(1L);
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        int databaseSizeBeforeCreate = productReviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductReviews() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get the productReview
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, productReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productReview.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductReview() throws Exception {
        // Get the productReview
        restProductReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview
        ProductReview updatedProductReview = productReviewRepository.findById(productReview.getId()).get();
        // Disconnect from session so that the updates on updatedProductReview are not directly saved in db
        em.detach(updatedProductReview);
        updatedProductReview.text(UPDATED_TEXT).rating(UPDATED_RATING).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(updatedProductReview);

        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProductReview.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview.text(UPDATED_TEXT).rating(UPDATED_RATING).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProductReview.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview.text(UPDATED_TEXT).rating(UPDATED_RATING).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProductReview.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productReviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // Create the ProductReview
        ProductReviewDTO productReviewDTO = productReviewMapper.toDto(productReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productReviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeDelete = productReviewRepository.findAll().size();

        // Delete the productReview
        restProductReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, productReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
