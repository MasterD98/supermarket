package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int quantity;
    private double unitPrice;
    private String category;
    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;

}
