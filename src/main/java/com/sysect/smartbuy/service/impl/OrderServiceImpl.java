package com.sysect.smartbuy.service.impl;

import com.sysect.smartbuy.domain.*;
import com.sysect.smartbuy.repository.*;
import com.sysect.smartbuy.security.SecurityUtils;
import com.sysect.smartbuy.service.OrderService;
import com.sysect.smartbuy.service.dto.OrderDTO;
import com.sysect.smartbuy.service.mapper.OrderMapper;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(
        OrderRepository orderRepository,
        OrderMapper orderMapper,
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        ProductRepository productRepository,
        OrderItemRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void placeOrder() {
        Order order = new Order();

        Cart cart = cartRepository
            .findCartByCustomer_User_Login(
                SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new SecurityException("User with given login not found!"))
            )
            .orElseThrow(() -> new EntityNotFoundException("Cart with given user not found!"));

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            Product product = cartItem.getProduct();

            orderItem.setPrice(product.getPrice());
            orderItem.setDiscountPrice(product.getDiscountPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setCreatedAt(Instant.now());
            orderItem.setUpdatedAt(Instant.now());

            orderItem.setProduct(product);

            order.addProducts(orderItem);
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Not enough of product in stock aborting placing order!!");
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            cartItemRepository.delete(cartItem);
        }

        orderRepository.saveAndFlush(order);
        orderItemRepository.saveAll(order.getProducts());

        cartItemRepository.deleteAll(cartItems);

        cart.setItems(new HashSet<>());
        cartRepository.saveAndFlush(cart);
    }
}
