package com.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.models.dao.IItemDao;
import com.springboot.models.entity.Carrito;
import com.springboot.models.entity.Item;
import com.springboot.models.services.ICarritoService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CarritoController {
	
	@Autowired
	private ICarritoService carritoService;
	
	
	@GetMapping("/item/all")
	public List<Item> findAllItem(){
		return carritoService.findAllItem();
	}
	
	@PostMapping("/item")
	public Item guardarItem(@RequestBody Item item) {
		return carritoService.save(item);
	}
	
	@PutMapping("/carrito/save")
	public Carrito guardarCarrito(@RequestBody Carrito carrito) {
		Carrito carritoUpdate = carritoService.findById(carrito.getId());
		List<Item> itemsOld = carritoUpdate.getItems();
		if(itemsOld.isEmpty()) {
			carritoUpdate.setItems(carrito.getItems());
		}else {
			itemsOld.addAll(carrito.getItems());
			carritoUpdate.setItems(itemsOld);
		}
		return carritoService.saveCarrito(carritoUpdate);
	}
	
	@GetMapping("/carrito/{id}")
	public Carrito buscarCarritoByID(@PathVariable Long id) {
		return carritoService.findById(id);
	}
}
