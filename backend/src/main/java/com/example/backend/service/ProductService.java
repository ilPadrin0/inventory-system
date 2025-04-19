package com.example.backend.service;

import java.util.List;

import com.example.backend.domain.Product;

public interface ProductService {
	List<Product> getAllProducts();
	Product getProductById(Long id);
	Product saveProduct(Product product);
	Product updateProduct(Long id, Product product);
	void deleteProduct(Long id);
}
