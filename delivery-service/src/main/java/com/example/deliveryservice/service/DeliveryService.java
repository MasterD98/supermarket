package com.example.deliveryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.deliveryservice.dto.DeliveryResponseDTO;
import com.example.deliveryservice.dto.DeliveryRequestDTO;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public String createNewDelivery(DeliveryRequestDTO deliveryRequestDTO) {

        Delivery newDelivery = Delivery.builder()
                .address(deliveryRequestDTO.getAddress())
                .deliveredStatus(deliveryRequestDTO.getDeliveredStatus())
                .deliveryCost(deliveryRequestDTO.getDeliveryCost())
                .build();
        deliveryRepository.save(newDelivery);
        return "delivery is successfully added";
    }

    public Page<DeliveryResponseDTO> getAllDeliveries(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Delivery> pageEntities = deliveryRepository.findAll(pageable);

        List<Delivery> entityList = pageEntities.getContent();
        List<DeliveryResponseDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> dtoList.add(convertDeliveryEntityToDeliveryResponse(entity)));

        return new PageImpl<>(dtoList, pageable, pageEntities.getTotalElements());
    }

    public DeliveryResponseDTO convertDeliveryEntityToDeliveryResponse(Delivery entity) {
        DeliveryResponseDTO deliveryResponseDTO = DeliveryResponseDTO.builder()
                .address(entity.getAddress())
                .deliveredStatus(entity.getDeliveredStatus())
                .deliveryCost(entity.getDeliveryCost())
                .build();
        return deliveryResponseDTO;
    }

    public DeliveryResponseDTO getDeliveryById(int id) {
        Delivery delivery = deliveryRepository.findById(id).orElse(null);
        DeliveryResponseDTO deliveryResponseDTO = DeliveryResponseDTO.builder()
                .address(delivery.getAddress())
                .deliveredStatus(delivery.getDeliveredStatus())
                .deliveryCost(delivery.getDeliveryCost())
                .id(delivery.getId())
                .build();
        return deliveryResponseDTO;
    }

    public String updateDelivery(DeliveryRequestDTO deliveryRequestDTO, int id) {

        Delivery updatedDelivery = deliveryRepository.findById(id).orElse(null);
        updatedDelivery.setAddress(deliveryRequestDTO.getAddress());
        updatedDelivery.setDeliveredStatus(deliveryRequestDTO.getDeliveredStatus());
        updatedDelivery.setDeliveryCost(deliveryRequestDTO.getDeliveryCost());
        updatedDelivery.save(updatedDelivery);
        return "Item is successfully updated";
    }

    public String deleteDelivery(int id) {
        deliveryRepository.deleteById(id);
        return "delivery successfully deleted";
    }
}
