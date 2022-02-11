package com.sysect.smartbuy.web.rest;

import com.sysect.smartbuy.repository.ProductReviewRepository;
import com.sysect.smartbuy.service.ProductReviewService;
import com.sysect.smartbuy.service.dto.ProductReviewDTO;
import com.sysect.smartbuy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sysect.smartbuy.domain.ProductReview}.
 */
@RestController
@RequestMapping("/api")
public class ProductReviewResource {

    private final Logger log = LoggerFactory.getLogger(ProductReviewResource.class);

    private static final String ENTITY_NAME = "productReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductReviewService productReviewService;

    private final ProductReviewRepository productReviewRepository;

    public ProductReviewResource(ProductReviewService productReviewService, ProductReviewRepository productReviewRepository) {
        this.productReviewService = productReviewService;
        this.productReviewRepository = productReviewRepository;
    }

    /**
     * {@code POST  /product-reviews} : Create a new productReview.
     *
     * @param productReviewDTO the productReviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productReviewDTO, or with status {@code 400 (Bad Request)} if the productReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-reviews")
    public ResponseEntity<ProductReviewDTO> createProductReview(@RequestBody ProductReviewDTO productReviewDTO) throws URISyntaxException {
        log.debug("REST request to save ProductReview : {}", productReviewDTO);
        if (productReviewDTO.getId() != null) {
            throw new BadRequestAlertException("A new productReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductReviewDTO result = productReviewService.save(productReviewDTO);
        return ResponseEntity
            .created(new URI("/api/product-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-reviews/:id} : Updates an existing productReview.
     *
     * @param id the id of the productReviewDTO to save.
     * @param productReviewDTO the productReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReviewDTO,
     * or with status {@code 400 (Bad Request)} if the productReviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-reviews/{id}")
    public ResponseEntity<ProductReviewDTO> updateProductReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReviewDTO productReviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductReview : {}, {}", id, productReviewDTO);
        if (productReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductReviewDTO result = productReviewService.save(productReviewDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReviewDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-reviews/:id} : Partial updates given fields of an existing productReview, field will ignore if it is null
     *
     * @param id the id of the productReviewDTO to save.
     * @param productReviewDTO the productReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReviewDTO,
     * or with status {@code 400 (Bad Request)} if the productReviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productReviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-reviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductReviewDTO> partialUpdateProductReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReviewDTO productReviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductReview partially : {}, {}", id, productReviewDTO);
        if (productReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductReviewDTO> result = productReviewService.partialUpdate(productReviewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReviewDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-reviews} : get all the productReviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productReviews in body.
     */
    @GetMapping("/product-reviews")
    public ResponseEntity<List<ProductReviewDTO>> getAllProductReviews(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductReviews");
        Page<ProductReviewDTO> page = productReviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-reviews/:id} : get the "id" productReview.
     *
     * @param id the id of the productReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-reviews/{id}")
    public ResponseEntity<ProductReviewDTO> getProductReview(@PathVariable Long id) {
        log.debug("REST request to get ProductReview : {}", id);
        Optional<ProductReviewDTO> productReviewDTO = productReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productReviewDTO);
    }

    /**
     * {@code DELETE  /product-reviews/:id} : delete the "id" productReview.
     *
     * @param id the id of the productReviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-reviews/{id}")
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long id) {
        log.debug("REST request to delete ProductReview : {}", id);
        productReviewService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
