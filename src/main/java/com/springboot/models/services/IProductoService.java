package com.springboot.models.services;

import java.util.List;

import com.springboot.models.entity.Producto;

public interface IProductoService {
	public List<Producto> findAll();
	
	public Producto findbyId(Long id);
	
	public Producto save(Producto producto);
	
	public void delete(Long id);
}
