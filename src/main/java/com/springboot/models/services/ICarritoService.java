package com.springboot.models.services;

import java.util.List;

import com.springboot.models.entity.Carrito;
import com.springboot.models.entity.Item;

public interface ICarritoService {
	
	public Item save(Item item);
	
	public List<Item> findAllItem();
	
	public void actualizarCantidadItem(Long id, int cantidad);
	
	public Carrito saveCarrito(Carrito carrito);
	
	public Carrito findById(Long id);
}
