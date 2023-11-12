package com.example.orderservice.service;

import com.example.orderservice.dto.OrderItemDTO;
import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient webClient;

    private OrderItem mapOrderItemDTOtoOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setName(orderItemDTO.getName());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setName(orderItemDTO.getName());
        orderItem.setDescription(orderItemDTO.getDescription());
        orderItem.setCategory(orderItemDTO.getCategory());
        return orderItem;
    }

    private OrderItemDTO mapOrderItemtoOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setName(orderItem.getName());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setName(orderItem.getName());
        orderItemDTO.setDescription(orderItem.getDescription());
        orderItemDTO.setCategory(orderItem.getCategory());
        return orderItemDTO;
    }

    public String placeOrder(OrderRequestDTO orderRequestDTO) {
        // get order items from request
        List<OrderItemDTO> orderItemDTOList = orderRequestDTO.getItems();
        // convert OrderItemsDTO to orderItems
        List<OrderItem> orderItems = orderItemDTOList.stream().map(this::mapOrderItemDTOtoOrderItem).toList();

        // create new order object
        Order newOrder = Order.builder()
                .customerId(orderRequestDTO.getCustomerId())
                .address(orderRequestDTO.getAddress())
                .total(orderRequestDTO.getTotal())
                .items(orderItems)
                .build();

        boolean allItemsValid = true;
        List<Boolean> results = new ArrayList<>();

        // check availability of all Items

        for (OrderItem orderItem : orderItems) {
            boolean isValid = validateItem(orderItem.getId(), orderItem.getQuantity());
            if (!isValid) {
                allItemsValid = false;
                break;
            }
        }

        // deduct amounts by each item

        if (allItemsValid) {
            for (OrderItem orderItem : orderItems) {
                boolean result = decreaseItemQuantity(orderItem.getId(), orderItem.getQuantity());
                results.add(result);
            }
            List<Boolean> failedItems = results.stream()
                    .filter(result -> result.equals(false)).toList();

            if (!failedItems.isEmpty()) {
                throw new IllegalArgumentException("Failed to decrease quantity");
            }
            orderRepository.save(newOrder);
            return "Order is successfully added";
        }
        return "Order adding unsuccessful";

    }

    private Boolean decreaseItemQuantity(int id, int quantity) {
        try {
            boolean response = webClient.put()
                    .uri("http://localhost:8091/inventory/decreaseItemQuantity?id=" + id
                            + "&quantity=" + quantity)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return response;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    private boolean validateItem(int id, int quantity) {
        try {
            Boolean response = webClient.get()
                    .uri("http://localhost:8091/inventory/validateItem?id=" + id + "&quantity="
                            + quantity)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return response;
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    public Page<OrderResponseDTO> getAllOrders(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Order> pageEntities = orderRepository.findAll(pageable);

        List<Order> entityList = pageEntities.getContent();
        List<OrderResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(convertOrderEntityToOrderResponse(entity)));

        return new PageImpl<>(dtoList, pageable, pageEntities.getTotalElements());
    }

    public OrderResponseDTO convertOrderEntityToOrderResponse(Order entity) {
        // get order items from request
        List<OrderItem> orderItemList = entity.getItems();
        List<OrderItemDTO> orderItemDTOs = orderItemList.stream().map(this::mapOrderItemtoOrderItemDTO).toList();
        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .customerId(entity.getCustomerId())
                .address(entity.getAddress())
                .total(entity.getTotal())
                .items(orderItemDTOs)
                .build();
        return orderResponseDTO;
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderItem> orderItems = order.getItems();
        List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(this::mapOrderItemtoOrderItemDTO).toList();
        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .customerId(order.getCustomerId())
                .address(order.getAddress())
                .total(order.getTotal())
                .items(orderItemDTOs)
                .id(order.getId())
                .build();
        return orderResponseDTO;
    }

    public String deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order successfully deleted";
    }
}
