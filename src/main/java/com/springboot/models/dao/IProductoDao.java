package com.springboot.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.models.entity.Marca;
import com.springboot.models.entity.Producto;

public interface IProductoDao  extends JpaRepository<Producto, Long>{
//HQL @Query
	
	@Query("from Marca")
	public List<Marca> findAllMarcas();

}
