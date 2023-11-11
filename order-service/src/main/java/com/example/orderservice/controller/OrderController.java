package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/addOrder")
    public ResponseEntity<String> addItem(@RequestBody OrderRequestDTO orderRequestDTO) {
        String result = orderService.placeOrder(orderRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<Page<OrderResponseDTO>> getAllItems(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {

        ResponseEntity<Page<OrderResponseDTO>> responseEntity = null;

        Page<OrderResponseDTO> pageDTOs = orderService.getAllItems(page, size);
        return responseEntity.ok(pageDTOs);
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<OrderResponseDTO> getItemById(@PathVariable(name = "id") int id) {
        OrderResponseDTO result = orderService.getOrderById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateOrder/{id}")
    public ResponseEntity<String> updateItem(@RequestBody OrderRequestDTO orderRequestDTO,
            @PathVariable(name = "id") int id) {
        String result = orderService.updateOrder(orderRequestDTO, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") int id) {
        String result = orderService.deleteOrder(id);
        return ResponseEntity.ok(result);
    }

}
