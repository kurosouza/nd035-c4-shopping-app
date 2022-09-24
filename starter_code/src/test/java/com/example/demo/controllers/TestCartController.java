package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

public class TestCartController {
	
	private CartController cartController;
	
	private CartRepository cartRepository = mock(CartRepository.class);
	
	private ItemRepository itemRepository = mock(ItemRepository.class);
	
	private UserRepository userRepository = mock(UserRepository.class);
	
	private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
	
	@Before
	public void setUp() {
		cartController = new CartController();
		
		TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
		TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
		TestUtils.injectObjects(cartController, "userRepository", userRepository);
	}	
	
	
	@Test
	public void testAddToCart() {
		Item item = createItem();
		User user = createUser();
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		
		ModifyCartRequest addToCartRequest = new ModifyCartRequest();
		
		addToCartRequest.setItemId(item.getId());
		addToCartRequest.setUsername(user.getUsername());
		addToCartRequest.setQuantity(2);
		
		ResponseEntity<Cart> response = cartController.addTocart(addToCartRequest);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		Cart cart = response.getBody();
		
		assertEquals(BigDecimal.valueOf(79.98), cart.getTotal());
		assertEquals(2, cart.getItems().size());
		assertEquals(item.getName(), cart.getItems().get(0).getName());
		
	}
	
	private Item createItem() {
		Item item = new Item();
		item.setId(1L);
		item.setName("turbo_controller");
		item.setDescription("Playstation 4 Enhanced Vibration Controller");
		item.setPrice(BigDecimal.valueOf(39.99));
		
		return item;
	}
	
	
	private User createUser() {
		User u = new User();
		u.setId(1);
		u.setUsername("test");
		u.setPassword("testpass");		
		Cart cart = new Cart();
		u.setCart(cart);
		return u;
	}
	
}
