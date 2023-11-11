package com.example.orderservice.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class OrderItemDTO {
    private int id;
    private int orderId;
    private String name;
    private String description;
    private int quantity;
    private double unitPrice;
    private String category;
}
