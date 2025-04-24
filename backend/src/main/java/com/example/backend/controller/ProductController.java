package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.Product;
import com.example.backend.dto.ProductDto;
import com.example.backend.dto.ProductStatisticsDto;
import com.example.backend.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}
	
	@GetMapping("/{id}")
    public Product getById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

	@PostMapping
	public Product create(@Valid @RequestBody ProductDto dto) {
	    Product product = new Product();
	    product.setName(dto.getName());
	    product.setQuantity(dto.getQuantity());
	    product.setPrice(dto.getPrice());
	    return productService.saveProduct(product);
	}
    
	@PutMapping("/{id}")
	public Product update(@PathVariable("id") Long id, @Valid @RequestBody ProductDto dto) {
	    Product product = new Product();
	    product.setId(id);
	    product.setName(dto.getName());
	    product.setQuantity(dto.getQuantity());
	    product.setPrice(dto.getPrice());
	    return productService.updateProduct(id, product);
	}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }
    
    @GetMapping("/statistics")
    public ProductStatisticsDto getStatistics() {
        return productService.getStatistics();
    }
    
    @GetMapping("/search")
    public List<Product> searchProducts(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(name = "order", defaultValue = "asc") String order,
        @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable
    ) {
        return productService.searchProducts(keyword, sortBy, order, onlyAvailable);
    }
}
