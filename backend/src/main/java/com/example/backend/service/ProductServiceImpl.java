package com.example.backend.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.backend.event.InventoryChangedEvent;

import com.example.backend.domain.Product;
import com.example.backend.dto.ProductStatisticsDto;
import com.example.backend.exception.ProductNotFoundException;
import com.example.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ApplicationEventPublisher publisher;
	
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	@Override
	public Product getProductById(Long id) {
	    return productRepository.findById(id)
	        .orElseThrow(() -> new ProductNotFoundException(id)); // ✅ 예외 던짐
	}
	
	@Override
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}
	
	@Override
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setQuantity(product.getQuantity());
        existing.setPrice(product.getPrice());
        
        Product saved =  productRepository.save(existing);
        
        publisher.publishEvent(
                new InventoryChangedEvent(saved.getId(), saved.getQuantity())
              );
              return saved;
    }
	
	@Override
	public void deleteProduct(Long id) {
		Product existing = getProductById(id);
		productRepository.deleteById(id);
	}
	
	@Override
	public ProductStatisticsDto getStatistics() {
	    List<Product> products = productRepository.findAll();

	    ProductStatisticsDto stats = new ProductStatisticsDto();
	    stats.setTotalProducts(products.size());
	    stats.setTotalQuantity(products.stream().mapToLong(Product::getQuantity).sum());
	    stats.setTotalValue(products.stream().mapToLong(p -> p.getQuantity() * p.getPrice()).sum());
	    stats.setLowStockCount(products.stream().filter(p -> p.getQuantity() > 0 && p.getQuantity() <= 5).count());
	    stats.setNoStockCount(products.stream().filter(p -> p.getQuantity() == 0).count());

	    return stats;
	}
	
	@Override
	public List<Product> searchProducts(String keyword, String sortBy, String order, Boolean onlyAvailable) {
	    List<Product> products = productRepository.findAll();

	    if (keyword != null && !keyword.isBlank()) {
	        products = products.stream()
	            .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
	            .collect(Collectors.toList());
	    }

	    if (onlyAvailable != null && onlyAvailable) {
	        products = products.stream()
	            .filter(p -> p.getQuantity() > 0)
	            .collect(Collectors.toList());
	    }

	    Comparator<Product> comparator;
	    switch (sortBy) {
	        case "price":
	            comparator = Comparator.comparing(Product::getPrice);
	            break;
	        case "quantity":
	            comparator = Comparator.comparing(Product::getQuantity);
	            break;
	        default:
	            comparator = Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
	    }

	    if ("desc".equalsIgnoreCase(order)) {
	        comparator = comparator.reversed();
	    }

	    return products.stream().sorted(comparator).collect(Collectors.toList());
	}
}
