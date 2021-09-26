package com.springboot.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.models.entity.Producto;

public interface IProductoDao  extends CrudRepository<Producto, Long>{
//HQL @Query
//Query methods

}
