package com.springboot.models.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entity.Producto;

public interface IProductoDao  extends JpaRepository<Producto, Long>{
//HQL @Query
//Query methods

}
