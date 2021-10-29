package com.springboot.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.models.entity.Compra;

public interface ICompraDao extends JpaRepository<Compra, Long>{
	
	@Query("select c from Compra c where c.usuario.id=?1")
	public List<Compra> obtenerComprasPorIdUsuario(Long id);
}
