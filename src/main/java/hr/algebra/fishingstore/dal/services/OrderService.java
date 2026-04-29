package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.OrderDto;
import hr.algebra.fishingstore.dal.repos.AddressRepository;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.Address;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public OrderDto.ResponseDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        return mapToResponse(order);
    }

    public List<OrderDto.ResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderDto.ResponseDto create(OrderDto.CreateDto dto){

        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Order order = new Order();
        order.setTotalPrice(dto.totalPrice());
        order.setOrderStatus(dto.orderStatus());
        order.setAddress(address);
        order.setUser(user);

        Order createdOrder = orderRepository.save(order);
        return mapToResponse(createdOrder);
    }

    public OrderDto.ResponseDto update(Long id,OrderDto.EditDto dto){
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order Not Found"));

        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        order.setTotalPrice(dto.totalPrice());
        order.setOrderStatus(dto.orderStatus());
        order.setAddress(address);
        order.setUser(user);

        Order editedOrder = orderRepository.save(order);
        return mapToResponse(editedOrder);
    }

    public boolean delete(Long id) {
        if(!orderRepository.existsById(id)) return false;

        orderRepository.deleteById(id);
        return true;
    }

    private OrderDto.ResponseDto mapToResponse(Order order) {
        return new OrderDto.ResponseDto(
                order.getId(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                order.getCreatedAt(),
                order.getAddress().getId(),
                order.getUser().getId()
        );
    }
}