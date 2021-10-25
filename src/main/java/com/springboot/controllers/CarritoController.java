package com.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/carrito")
	public Carrito guardarCarrito(@RequestBody Carrito carrito) {
		return carritoService.saveCarrito(carrito);
	} 
}
