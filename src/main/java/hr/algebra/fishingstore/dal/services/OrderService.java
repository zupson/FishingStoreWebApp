package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.repos.*;
import hr.algebra.fishingstore.model.entities.*;
import hr.algebra.fishingstore.model.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    public static final String ORDER_NOT_FOUND = "Order Not Found";
    public static final String USER_NOT_FOUND = "User Not Found";
    public static final String ADDRESS_NOT_FOUND = "Address Not Found";
    public static final String CART_NOT_FOUND = "Cart Not Found";
    public static final String CART_IS_EMPTY = "Cart is empty";

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductOrderRepository productOrderRepository;

    public OrderDto.ResponseDto getById(Long id) {
        return mapToResponse(orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND)));
    }

    public List<OrderDto.ResponseDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public OrderDto.ResponseDto create(OrderDto.CreateDto dto) {
        String currentLoggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentLoggedInUsername)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException(ADDRESS_NOT_FOUND));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException(CART_NOT_FOUND));
        List<CartProduct> cartProducts = cartProductRepository.findByCart(cart);

        if (cartProducts.isEmpty()) throw new RuntimeException(CART_IS_EMPTY);

        BigDecimal totalPrice = cartProducts
                .stream()
                .map(product -> product
                        .getProduct()
                        .getPrice()
                        .multiply(BigDecimal
                                .valueOf(product
                                        .getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAddress(address);
        order.setUser(user);
        order.setTotalPrice(totalPrice);

        Order createdOrder = orderRepository.save(order);

        for (CartProduct item : cartProducts) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(createdOrder);
            productOrder.setProduct(item.getProduct());
            productOrder.setQuantity(item.getQuantity());
            productOrder.setPriceAtPurchase(item.getProduct().getPrice());
            productOrderRepository.save(productOrder);
        }

        cartProductRepository.deleteAll(cartProducts);
        return mapToResponse(createdOrder);
    }

    public OrderDto.ResponseDto update(Long id, OrderDto.EditDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));

        order.setOrderStatus(dto.getOrderStatus());

        return mapToResponse(orderRepository.save(order));
    }

    public boolean delete(Long id) {
        if (!orderRepository.existsById(id)) return false;

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