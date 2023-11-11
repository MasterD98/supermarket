package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private final WebClient webClient;

    public String placeOrder(OrderRequestDTO orderRequestDTO) {

        Order newOrder = Order.builder()
                .customerId(orderRequestDTO.getCustomerId())
                .address(orderRequestDTO.getAddress())
                .total(orderRequestDTO.getTotal())
                .items(orderRequestDTO.getItems())
                .build();
        orderRepository.save(newOrder);
        return "Order is successfully added";
    }

    public Page<OrderResponseDTO> getAllOrders(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Order> pageEntities = orderRepository.findAll(pageable);

        List<Order> entityList = pageEntities.getContent();
        List<OrderResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(convertItemEntityToItemResponse(entity)));

        return new PageImpl<>(dtoList, pageable, pageEntities.getTotalElements());
    }

    public OrderResponseDTO convertItemEntityToItemResponse(Order entity) {
        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .customerId(entity.getCustomerId())
                .address(entity.getAddress())
                .total(entity.getTotal())
                .items(entity.getItems())
                .build();
        return orderResponseDTO;
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .customerId(order.getCustomerId())
                .address(order.getAddress())
                .total(order.getTotal())
                .items(order.getItems())
                .id(order.getId())
                .build();
        return orderResponseDTO;
    }

    public String updateOrder(OrderRequestDTO orderRequestDTO, int id) {

        Order updatedOrder = orderRepository.findById(id).orElse(null);
        updatedOrder.setCustomerId(orderRequestDTO.getCustomerId());
        updatedOrder.setAddress(orderRequestDTO.getAddress());
        updatedOrder.setTotal(orderRequestDTO.getTotal());
        updatedOrder.setItems(orderRequestDTO.getItems());
        orderRepository.save(updatedOrder);
        return "order successfully updated";
    }

    public String deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order successfully deleted";
    }
}
