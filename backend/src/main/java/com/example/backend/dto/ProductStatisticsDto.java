package com.example.backend.dto;

import lombok.Data;

@Data
public class ProductStatisticsDto {
	private long totalProducts;
    private long totalQuantity;
    private long totalValue;
    private long lowStockCount;
    private long noStockCount;
}
