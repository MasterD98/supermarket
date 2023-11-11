package com.example.deliveryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.deliveryservice.dto.DeliveryRequestDTO;
import com.example.deliveryservice.dto.DeliveryResponseDTO;
import com.example.deliveryservice.service.DeliveryService;

@RestController()
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/createDelivery")
    public ResponseEntity<String> addItem(@RequestBody DeliveryRequestDTO itemRequestDTO) {
        String result = deliveryService.createNewDelivery(itemRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllDeliveries")
    public ResponseEntity<Page<DeliveryResponseDTO>> getAllItems(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {

        ResponseEntity<Page<DeliveryResponseDTO>> responseEntity = null;

        Page<DeliveryResponseDTO> pageDTOs = deliveryService.getAllDeliveries(page, size);
        return responseEntity.ok(pageDTOs);
    }

    @GetMapping("/getDelivery/{id}")
    public ResponseEntity<DeliveryResponseDTO> getItemById(@PathVariable(name = "id") int id) {
        DeliveryResponseDTO result = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/updateDelivery/{id}")
    public ResponseEntity<String> updateItem(@RequestBody DeliveryRequestDTO deliveryRequestDTO,
            @PathVariable(name = "id") int id) {
        String result = deliveryService.updateDelivery(deliveryRequestDTO, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("deleteDelivery/{id}")
    public ResponseEntity<String> deleteDelivery(@PathVariable(name = "id") int id) {
        String result = deliveryService.deleteDelivery(id);
        return ResponseEntity.ok(result);
    }

}
