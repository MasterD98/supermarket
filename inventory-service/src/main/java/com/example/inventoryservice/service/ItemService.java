package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.ItemRequestDTO;
import com.example.inventoryservice.dto.ItemResponseDTO;
import com.example.inventoryservice.entity.Item;
import com.example.inventoryservice.repository.ItemRepository;
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
    private ItemRepository itemRepository;

    /**
     * Returns an String.
     * <p>
     * This method add new item to database
     *
     * @param itemRequestDTO an item request object
     * @return String
     * 
     */
    public String addNewItem(ItemRequestDTO itemRequestDTO) {
        // create new item
        Item newItem = Item.builder()
                .name(itemRequestDTO.getName())
                .category(itemRequestDTO.getCategory())
                .description(itemRequestDTO.getDescription())
                .unitPrice(itemRequestDTO.getUnitPrice())
                .image(itemRequestDTO.getImage())
                .availableQuantity(itemRequestDTO.getAvailableQuantity())
                .build();
        // save new item to database
        itemRepository.save(newItem);
        return "Item is successfully added to the inventory";
    }

    public Page<ItemResponseDTO> getAllItems(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Item> pageEntities = itemRepository.findAll(pageable);

        List<Item> entityList = pageEntities.getContent();
        List<ItemResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(convertItemEntityToItemResponse(entity)));

        return new PageImpl<>(dtoList, pageable, pageEntities.getTotalElements());
    }

    public ItemResponseDTO convertItemEntityToItemResponse(Item entity) {
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
        Item item = itemRepository.findById(id).orElse(null);
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

        Item updatedItem = itemRepository.findById(id).orElse(null);

        if (updatedItem == null) {
            return "Item not Found";
        }
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

    public Boolean validateItem(int id, int quantity) {
        Item item = itemRepository.findById(id).orElse(null);

        if (item == null) {
            return false;
        }
        if (item.getAvailableQuantity() < quantity) {
            return false;
        }

        return true;
    }

    public Boolean decreaseItemQuantity(int id, int quantity) {
        Item item = itemRepository.findById(id).orElse(null);

        if (item == null) {
            return false;
        }
        if (item.getAvailableQuantity() < quantity) {
            return false;
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - quantity);
        itemRepository.save(item);
        return true;
    }

    public Boolean increaseItemQuantity(int id, int quantity) {
        Item item = itemRepository.findById(id).orElse(null);

        if (item == null) {
            return false;
        }

        item.setAvailableQuantity(item.getAvailableQuantity() + quantity);
        itemRepository.save(item);
        return true;
    }
}
