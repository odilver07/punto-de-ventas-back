package com.springboot.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entity.Item;

public interface IItemDao extends JpaRepository<Item, Long>{

}
