package com.springboot.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.springboot.models.entity.Item;

public interface IItemDao extends JpaRepository<Item, Long>{
	
	@Modifying
	@Query("update Item i set i.cantidad = ?2 where i.id=?1")
	void actualizarCantidadItem(Long id, int cantidad);

}
