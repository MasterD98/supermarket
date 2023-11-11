package com.example.orderservice.entity;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
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
    private Date date;
    @OneToMany
    private List<OrderItem> items;

}
