package com.springboot.models.services;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.models.entity.Marca;
import com.springboot.models.entity.Producto;

public interface IProductoService {
	public List<Producto> findAll();
	
	public Page<Producto> findAll(Pageable pageable);
	
	public Producto findbyId(Long id);
	
	public Producto save(Producto producto);
	
	public void delete(Long id);
	
	public List<Marca> findAllMarcas();
}
