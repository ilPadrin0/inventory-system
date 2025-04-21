package com.example.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDto {

    @NotBlank(message = "상품명은 비어 있을 수 없습니다.")
    private String name;

    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private int quantity;

    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    private int price;
}
