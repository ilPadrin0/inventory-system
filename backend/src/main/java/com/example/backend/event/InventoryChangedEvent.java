package com.example.backend.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InventoryChangedEvent {
    // 변경된 상품 ID
    private final Long productId;
    // 변경 후 재고 수량
    private final int newQuantity;
}
