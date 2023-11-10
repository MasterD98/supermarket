package com.example.inventoryservice.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class OrderRequestDTO {
    private int id;
    private String name;
    private String description;
    private String image;
    private int availableQuantity;
    private double unitPrice;
    private String category;
}
