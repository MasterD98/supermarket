package com.example.orderservice.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Table(name = "_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customerId;
    private String address;
    private double total;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

}
