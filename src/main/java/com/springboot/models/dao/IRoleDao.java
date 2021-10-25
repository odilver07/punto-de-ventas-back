package com.springboot.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entity.Role;

public interface IRoleDao extends JpaRepository<Role, Long>{

}
