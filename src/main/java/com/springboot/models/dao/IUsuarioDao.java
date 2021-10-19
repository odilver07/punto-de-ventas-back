package com.springboot.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.springboot.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	@Query("select u from Usuario u where  u.username=?1")
	public Usuario findByUsernameQuery(String username);
	
	public Usuario findByUsername(String username);
	
	public Usuario findByEmail(String email);
}
