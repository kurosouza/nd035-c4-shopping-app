package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {
	
	private UserController userController;
	
	private UserRepository userRepository = mock(UserRepository.class);

	private CartRepository  cartRepository = mock(CartRepository.class);
	
	private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
	
	@Before
	public void setUp() {
		userController = new UserController();
		TestUtils.injectObjects(userController, "userRepository", userRepository);
		TestUtils.injectObjects(userController, "cartRepository", cartRepository);
		TestUtils.injectObjects(userController, "passwordEncoder", passwordEncoder);
	}
	
	@Test
	public void testCreateUser() throws Exception {
		when(passwordEncoder.encode("testpass")).thenReturn("test-pass-hashed");
		
		CreateUserRequest r = getCreateUserRequest();
		
		final ResponseEntity<User> response = userController.createUser(r);
		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertEquals(0, u.getId());
		assertEquals("test", u.getUsername());
		assertEquals("test-pass-hashed", u.getPassword());		
	}
	
	@Test
	public void testGetUserByID() {
		User testUser = getTestUser();
		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));		
		ResponseEntity<User> response = userController.findById(1L);		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertEquals(1, u.getId());
		assertEquals("test", u.getUsername());
	}
	
	@Test
	public void testGetUserByUsername() {
		User testUser = getTestUser();
		when(userRepository.findByUsername("test")).thenReturn(testUser);		
		ResponseEntity<User> response = userController.findByUserName("test");		
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		User u = response.getBody();
		assertEquals(1, u.getId());
		assertEquals("test", u.getUsername());
	}
	
	private User getTestUser() {
		User u = new User();
		u.setId(1);
		u.setUsername("test");
		u.setPassword("testpass");
		
		return u;
	}
	
	private CreateUserRequest getCreateUserRequest() {
		CreateUserRequest r = new CreateUserRequest();
		r.setUsername("test");
		r.setPassword("testpass");
		r.setConfirmPassword("testpass");
		
		return r;
	}
}
