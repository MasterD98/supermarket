package com.example.deliveryservice.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class DeliveryResponseDTO {
    private int id;
    private String address;
    private String deliveredStatus;
    private double deliveryCost;
}
