package com.springboot.models.services;

import com.springboot.models.entity.Item;
import com.springboot.models.entity.Role;
import com.springboot.models.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);
	
	public Usuario guardarUsuario(Usuario usuario);
	
	public Role findByRole(Long id);
	
	public Usuario findById(Long id);
}
