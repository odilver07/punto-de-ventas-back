package com.springboot.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.models.dao.ICarritoDao;
import com.springboot.models.dao.ICompraDao;
import com.springboot.models.dao.IItemDao;
import com.springboot.models.entity.Carrito;
import com.springboot.models.entity.Compra;
import com.springboot.models.entity.Item;

@Service
public class CarritoServiceImpl implements ICarritoService{
	
	@Autowired
	private ICarritoDao carritoDao;
	
	@Autowired
	private IItemDao itemDao;
	
	@Autowired
	private ICompraDao compraDao;
	
	@Override
	public Item save(Item item) {
		return itemDao.save(item);
	}

	@Override
	public List<Item> findAllItem() {
		return itemDao.findAll();
	}

	@Override
	public Carrito saveCarrito(Carrito carrito) {
		return carritoDao.save(carrito);
	}

	@Override
	public Carrito findById(Long id) {
		return carritoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void actualizarCantidadItem(Long id, int cantidad) {
		itemDao.actualizarCantidadItem(id, cantidad);
	}

	@Override
	public void vaciarCarrito(Long id) {
		Carrito carritoUp =  carritoDao.findById(id).orElse(null);
		carritoUp.getItems().clear();;
		carritoDao.save(carritoUp);
	}

	@Override
	public Compra guardarCompra(Compra compra) {
		return compraDao.save(compra);
	}


}
