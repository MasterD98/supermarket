package com.example.orderservice.service;

import com.example.inventoryservice.dto.ItemRequestDTO;
import com.example.inventoryservice.dto.ItemResponseDTO;
import com.example.inventoryservice.entity.Item;
import com.example.inventoryservice.repository.ItemRepository;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public String addNewItem(ItemRequestDTO itemRequestDTO) {

        Order newItem = Order.builder()
                .name(itemRequestDTO.getName())
                .category(itemRequestDTO.getCategory())
                .description(itemRequestDTO.getDescription())
                .unitPrice(itemRequestDTO.getUnitPrice())
                .image(itemRequestDTO.getImage())
                .availableQuantity(itemRequestDTO.getAvailableQuantity())
                .build();
        itemRepository.save(newItem);
        return "Item is successfully added to the inventory";
    }

    public Page<ItemResponseDTO> getAllItems(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Order> pageEntities = itemRepository.findAll(pageable);

        List<Order> entityList = pageEntities.getContent();
        List<ItemResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(convertItemEntityToItemResponse(entity)));

        return new PageImpl<>(dtoList, pageable, pageEntities.getTotalElements());
    }

    public ItemResponseDTO convertItemEntityToItemResponse(Order entity) {
        ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .availableQuantity(entity.getAvailableQuantity())
                .unitPrice(entity.getUnitPrice())
                .id(entity.getId())
                .description(entity.getDescription())
                .image(entity.getImage())
                .build();
        return itemResponseDTO;
    }

    public ItemResponseDTO getItemById(int id) {
        Order item = itemRepository.findById(id).orElse(null);
        ItemResponseDTO itemResponseDTO = ItemResponseDTO.builder()
                .name(item.getName())
                .category(item.getCategory())
                .availableQuantity(item.getAvailableQuantity())
                .unitPrice(item.getUnitPrice())
                .description(item.getDescription())
                .image(item.getImage())
                .id(item.getId())
                .build();
        return itemResponseDTO;
    }

    public String updateItem(ItemRequestDTO itemRequestDTO, int id) {

        Order updatedItem = itemRepository.findById(id).orElse(null);
        updatedItem.setName(itemRequestDTO.getName());
        updatedItem.setCategory(itemRequestDTO.getCategory());
        updatedItem.setUnitPrice(itemRequestDTO.getUnitPrice());
        updatedItem.setDescription(itemRequestDTO.getDescription());
        updatedItem.setAvailableQuantity(itemRequestDTO.getAvailableQuantity());
        updatedItem.setImage(itemRequestDTO.getImage());
        itemRepository.save(updatedItem);
        return "Item is successfully updated";
    }

    public String deleteItem(int id) {
        itemRepository.deleteById(id);
        return "Item is successfully deleted";
    }
}
