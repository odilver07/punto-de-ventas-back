package com.springboot.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entity.Carrito;

public interface ICarritoDao extends JpaRepository<Carrito, Long>{
	
}
