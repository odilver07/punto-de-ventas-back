package com.springboot.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.models.dao.ICarritoDao;
import com.springboot.models.dao.IItemDao;
import com.springboot.models.entity.Carrito;
import com.springboot.models.entity.Item;

@Service
public class CarritoServiceImpl implements ICarritoService{
	
	@Autowired
	private ICarritoDao carritoDao;
	
	@Autowired
	private IItemDao itemDao;
	
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

}
