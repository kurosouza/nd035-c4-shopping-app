package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

public class ItemControllerTest {
	
	private ItemController itemController;
	
	private ItemRepository itemRepository = mock(ItemRepository.class);
	
	@Before
	public void setUp() {
		itemController = new ItemController();
		TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
	}
	
	@Test
	public void testGetItems() {
		Item item = createItem();
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
		final ResponseEntity<Item> response = itemController.getItemById(1L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		Item i = response.getBody();
		
		assertEquals(item.getId(), i.getId());
		assertEquals(item.getName(), i.getName());
		assertEquals(item.getPrice(), i.getPrice());
		
	}
	
	@Test
	public void testGetAllItems() {
		Item item = createItem();
		when(itemRepository.findAll()).thenReturn(List.of(item));
		
		final ResponseEntity<List<Item>> response = itemController.getItems();
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		
		List<Item> items = response.getBody();
		
		assertEquals(1, items.size());
		assertEquals(item.getId(), items.get(0).getId());
		assertEquals(item.getName(), items.get(0).getName());
		assertEquals(item.getPrice(), items.get(0).getPrice());
		
	}
	
	private Item createItem() {
		Item item = new Item();
		item.setId(1L);
		item.setName("Turbo Controller");
		item.setDescription("Playstation 4 Enhanced Vibration Controller");
		item.setPrice(BigDecimal.valueOf(39.99));
		
		return item;

	}

}
