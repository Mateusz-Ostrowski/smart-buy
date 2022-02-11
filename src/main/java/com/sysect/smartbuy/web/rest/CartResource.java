package com.sysect.smartbuy.web.rest;

import com.sysect.smartbuy.repository.CartRepository;
import com.sysect.smartbuy.security.SecurityUtils;
import com.sysect.smartbuy.service.CartItemService;
import com.sysect.smartbuy.service.CartService;
import com.sysect.smartbuy.service.dto.CartDTO;
import com.sysect.smartbuy.service.dto.CartItemDTO;
import com.sysect.smartbuy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.sysect.smartbuy.web.rest.vm.AddItemToCartVM;
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
 * REST controller for managing {@link com.sysect.smartbuy.domain.Cart}.
 */
@RestController
@RequestMapping("/api")
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private static final String ENTITY_NAME = "cart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartService cartService;

    private final CartItemService cartItemService;

    private final CartRepository cartRepository;


    public CartResource(CartService cartService, CartItemService cartItemService, CartRepository cartRepository) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.cartRepository = cartRepository;
    }

    /**
     * {@code POST  /carts} : Create a new cart.
     *
     * @param cartDTO the cartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartDTO, or with status {@code 400 (Bad Request)} if the cart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carts")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) throws URISyntaxException {
        log.debug("REST request to save Cart : {}", cartDTO);
        if (cartDTO.getId() != null) {
            throw new BadRequestAlertException("A new cart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartDTO result = cartService.save(cartDTO);
        return ResponseEntity
            .created(new URI("/api/carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /carts/:id} : Updates an existing cart.
     *
     * @param id the id of the cartDTO to save.
     * @param cartDTO the cartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartDTO,
     * or with status {@code 400 (Bad Request)} if the cartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carts/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable(value = "id", required = false) final Long id, @RequestBody CartDTO cartDTO)
        throws URISyntaxException {
        log.debug("REST request to update Cart : {}, {}", id, cartDTO);
        if (cartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CartDTO result = cartService.save(cartDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /carts/:id} : Partial updates given fields of an existing cart, field will ignore if it is null
     *
     * @param id the id of the cartDTO to save.
     * @param cartDTO the cartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartDTO,
     * or with status {@code 400 (Bad Request)} if the cartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CartDTO> partialUpdateCart(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartDTO cartDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cart partially : {}, {}", id, cartDTO);
        if (cartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CartDTO> result = cartService.partialUpdate(cartDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /carts} : get all the carts.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carts in body.
     */
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("customer-is-null".equals(filter)) {
            log.debug("REST request to get all Carts where customer is null");
            return new ResponseEntity<>(cartService.findAllWhereCustomerIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Carts");
        Page<CartDTO> page = cartService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /carts/:id} : get the "id" cart.
     *
     * @param id the id of the cartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carts/{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long id) {
        log.debug("REST request to get Cart : {}", id);
        Optional<CartDTO> cartDTO = cartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartDTO);
    }

    /**
     * {@code DELETE  /carts/:id} : delete the "id" cart.
     *
     * @param id the id of the cartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carts/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        log.debug("REST request to delete Cart : {}", id);
        cartService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    //Returns list of cart items for logged in user

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDTO>> getCart(){
        log.debug("REST request to get users Cart : {}", SecurityUtils.getCurrentUserLogin().orElse("Not logged in"));
        return ResponseEntity.ok(cartItemService.findAllCartItemsOfCurrentUser());
    }

    @PostMapping("/cart")
    public ResponseEntity<Void> addItemToCart(@RequestBody AddItemToCartVM addItemToCartVM){
        cartService.addItemToCart(addItemToCartVM);
        return ResponseEntity.ok().build();
    }
}
