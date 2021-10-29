package com.springboot.controllers;

import java.util.ArrayList;
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
import com.springboot.models.entity.Compra;
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
		System.out.println(itemsOld);
		if(itemsOld.isEmpty()) {
			carritoUpdate.setItems(carrito.getItems());
			return carritoService.saveCarrito(carritoUpdate);
		}else {
			boolean unico = true;
			for(int i = 0; i < carrito.getItems().size(); i++) {
				unico = true;
				for(int k = 0; k < itemsOld.size(); k++) {
					if(carrito.getItems().get(i).getId() == itemsOld.get(k).getId()) {
						System.out.println(carrito.getItems().get(i).getId());
						System.out.println(carrito.getItems().get(i).getCantidad());
						System.out.println(itemsOld.get(k).getCantidad());
						carritoService.actualizarCantidadItem(carrito.getItems().get(i).getId(), 
								carrito.getItems().get(i).getCantidad() + itemsOld.get(k).getCantidad());
						unico = false;
					}
				}
				if(unico) {
					carritoUpdate.getItems().add(carrito.getItems().get(i));
				}
			}
		}
		return carritoService.saveCarrito(carritoUpdate);
	}
	
	@GetMapping("/carrito/{id}")
	public Carrito buscarCarritoByID(@PathVariable Long id) {
		Carrito carritoNew = carritoService.findById(id);
		for(int i = 0; i < carritoNew.getItems().size(); i++) {
			for(int k = 0; k < carritoNew.getItems().size(); k++) {
				if(carritoNew.getItems().get(i).getProducto().getId() ==
					carritoNew.getItems().get(k).getProducto().getId() && i!=k) {
					carritoService.actualizarCantidadItem(carritoNew.getItems().get(i).getId(),
							carritoNew.getItems().get(i).getCantidad() + 
							carritoNew.getItems().get(i).getCantidad() );
					carritoNew.getItems().remove(k);
				}
			}
		}
		carritoService.saveCarrito(carritoNew);
		return carritoService.findById(id);
		
	}
	
	@PutMapping("/vaciar/carrito/{id}")
	public void vaciarCarrito(@PathVariable Long id) {
		carritoService.vaciarCarrito(id);
	}
	
	@PostMapping("/guardar/compra")
	public Compra guardarCompra(@RequestBody Compra compra) {
		Compra compraReturn =  carritoService.guardarCompra(compra);
		carritoService.vaciarCarrito(compra.getUsuario().getId());
		return compraReturn;
	}
	
	@GetMapping("/mis-compras/{id}")
	public List<Compra> obtenerComprasPorIdU(@PathVariable Long id){
		return carritoService.obtenerComprasPorIdUsuario(id);
	}
	
	
	
}
