package com.springboot.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.models.entity.Carrito;
import com.springboot.models.entity.Role;
import com.springboot.models.entity.Usuario;
import com.springboot.models.services.ICarritoService;
import com.springboot.models.services.IUsuarioService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UsurioRestController {
	
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private  IUsuarioService usuarioService;
	
	@Autowired
	private ICarritoService carritoService;
	
	@PostMapping("/save/user")
	public Usuario guardarUsuario(@RequestBody Usuario usuario) {
		String pass = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(pass);
		Role role = usuarioService.findByRole(1L);
		System.out.println(role);
		List<Role> roles =  new ArrayList<Role>();
		roles.add(role);
		usuario.setRoles(roles);
		Carrito carrito = new Carrito();
		carrito = carritoService.saveCarrito(carrito);
		usuario.setCarrito(carrito);
		return usuarioService.guardarUsuario(usuario);
	}
	
	@PutMapping("/usuario/direccion/{id}/{direccion}")
	public Usuario actualizarDireccion(@PathVariable Long id, @PathVariable String direccion) {
		Usuario usuarioNew  = usuarioService.findById(id);
		usuarioNew.setDireccion(direccion);
		return usuarioService.guardarUsuario(usuarioNew);
		
	}
}
