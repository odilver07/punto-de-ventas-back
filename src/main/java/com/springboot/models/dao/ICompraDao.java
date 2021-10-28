package com.springboot.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entity.Compra;

public interface ICompraDao extends JpaRepository<Compra, Long>{

}
