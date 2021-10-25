package com.springboot.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.models.entity.Marca;
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
	
	@GetMapping("/productos/page/{page}")
	public Page<Producto> productosDisponiblesPorPagina(@PathVariable int page){
		return productoService.findAll(PageRequest.of(page, 10));
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
		
		if(producto.getNombre().length() < 4 || producto.getNombre().length() > 55) {
			request.put("mensaje", "Agregue un nombre correcto, debe tener entre 4 a 60 caracteres");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//Validar cantidades
		if(producto.getCantidad() < 1) {
			request.put("mensaje", "Agregue una cantidad adecuada no seas wey :v");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(producto.getPrecio() <= 0) {
			request.put("mensaje", "Agregue un precio adecuado para el producto");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//validar que haya conexion con la base de datos
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
	public ResponseEntity<?> deleteProductoById(@PathVariable Long id) {
		Producto producto =  productoService.findbyId(id);
		Map<String, Object> request =  new HashMap<>();
		
		if(producto == null) {
			request.put("mensaje", "El producto no existe");
			return new ResponseEntity<Map<String, Object>>(request,HttpStatus.NOT_FOUND);
		}
		try {
			String nombreFotoAnterior =  producto.getFoto();
			
			if(nombreFotoAnterior != null  && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnteorior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior =  rutaFotoAnteorior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			productoService.delete(id);
		}catch (DataAccessException e) {
			request.put("mensaje", "No se pudo eliminar el producto");
			request.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(request,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		request.put("mensaje", "Se elimino el producto ".concat(producto.getNombre()));
		return new ResponseEntity<Map<String, Object>>(request,HttpStatus.NO_CONTENT);
		
	}
	
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> productoUpdate(@RequestBody Producto producto, @PathVariable Long id) {
		Map<String, Object> request =  new HashMap<>();
		
		if(producto.getCantidad() < 1) {
			request.put("mensaje", "Agregue una cantidad adecuada para el producto");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(producto.getPrecio() <= 0) {
			request.put("mensaje", "Agregue un precio adecuado para el producto");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		Producto productoActual = productoService.findbyId(id);
		Producto pUpdate = productoService.findbyId(id);
		
		if(productoActual == null) {
			request.put("mensaje", "Error: Mo se pudo editar el producto");
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.NOT_FOUND);
		}
		
		try {
			productoActual.setCantidad(producto.getCantidad());
			productoActual.setDescripcion(producto.getDescripcion());
			productoActual.setNombre(producto.getNombre());
			productoActual.setPrecio(producto.getPrecio());
			productoActual.setMarca(producto.getMarca());
			
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
	
	@PostMapping("/productos/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String, Object> request =  new HashMap<>();
		Producto producto = productoService.findbyId(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString().concat("_").concat(archivo.getOriginalFilename().replace(" ", ""));
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				request.put("mensaje","Error al subir la imagen");
				return new ResponseEntity<Map<String, Object>>(request, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior =  producto.getFoto();
			
			if(nombreFotoAnterior != null  && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnteorior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior =  rutaFotoAnteorior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			producto.setFoto(nombreArchivo);
			productoService.save(producto);
			request.put("producto", producto);
			request.put("mensaje", "Has subido correctamente "+ archivo.getOriginalFilename());
		}
		return new ResponseEntity<Map<String, Object>>(request,HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verfoto(@PathVariable String nombreFoto){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		Resource recurso = null;
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Errro no se pudo cargar la imagen "+ nombreFoto);
		}
		HttpHeaders  cabecera =  new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; file=\""+recurso.getFilename()+"\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
	@GetMapping("/marcas")
	public List<Marca> listarMarcas(){
		return productoService.findAllMarcas();
	}
	
	@GetMapping("/productos/disponibles/{term}")
	public List<Producto> buscarProductoPorNombre(@PathVariable String term){
		return productoService.findByNombre(term);
	}
}
