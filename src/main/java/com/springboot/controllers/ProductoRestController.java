package com.springboot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.models.entity.Producto;
import com.springboot.models.services.IProductoService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductoRestController {
	
	@Autowired
	private IProductoService productoService;
	
	@GetMapping("/productos")
	public List<Producto> productosDisponibles(){
		return productoService.findAll();
	}
	
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> findByIdProducto(@PathVariable Long id) {
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			producto = productoService.findbyId(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error en realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(producto == null) {
			response.put("mensaje", "El producto con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto, HttpStatus.OK) ;
	}
	
	@PostMapping("/productos")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveProducto(@RequestBody Producto producto) {
		Producto productoNew = null;
		
		Map<String, Object> request =  new HashMap<>();
		try {
			productoNew = productoService.save(producto);
		}catch(DataAccessException e) {
			request.put("mensaje","Error al guardar en la base de datos");
			request.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		request.put("mensaje", "El producto se ha creado con exito");
		request.put("producto", productoNew);
		return new ResponseEntity<Map<String, Object>>(request,HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/productos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProductoById(@PathVariable Long id) {
		productoService.delete(id);
	}
	
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> productoUpdate(@RequestBody Producto producto, @PathVariable Long id) {
		Producto productoActual = productoService.findbyId(id);
		Producto pUpdate = productoService.findbyId(id);
		Map<String, Object> request =  new HashMap<>();
		
		if(productoActual == null) {
			request.put("mensaje", "Error: Mo se pudo editar el producto");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.NOT_FOUND);
		}
		
		try {
			productoActual.setCantidad(producto.getCantidad());
			productoActual.setDescripcion(producto.getDescripcion());
			productoActual.setNombre(producto.getNombre());
			productoActual.setPrecio(producto.getPrecio());
			
			pUpdate =  productoService.save(producto);
		}catch (DataAccessException e) {
			request.put("mensaje","Error al actualizar en la base de datos");
			request.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		request.put("mensaje", "El producto se ha actualizado con exito");
		request.put("producto", pUpdate);
		return new ResponseEntity<Map<String, Object>>(request,HttpStatus.CREATED);
	}
}
