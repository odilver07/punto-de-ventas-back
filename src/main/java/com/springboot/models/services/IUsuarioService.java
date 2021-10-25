package com.springboot.models.services;

import com.springboot.models.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);
	
	public Usuario guardarUsuario(Usuario usuario);

}
