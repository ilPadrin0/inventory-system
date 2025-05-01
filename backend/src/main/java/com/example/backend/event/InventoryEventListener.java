package com.example.backend.event;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryEventListener {
    private final SimpMessagingTemplate template;

    @EventListener
    public void onInventoryChanged(InventoryChangedEvent event) {
        template.convertAndSend(
          "/topic/inventory",
          Map.of(
            "productId", event.getProductId(),
            "newQuantity", event.getNewQuantity()
          )
        );
    }
}
