package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.repos.*;
import hr.algebra.fishingstore.dal.specifications.OrderSpecification;
import hr.algebra.fishingstore.exceptions.CartEmptyException;
import hr.algebra.fishingstore.model.entities.*;
import hr.algebra.fishingstore.model.enums.Currency;
import hr.algebra.fishingstore.model.enums.OrderStatus;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
import hr.algebra.fishingstore.model.enums.Role;
import hr.algebra.fishingstore.session.CartSession;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static hr.algebra.fishingstore.dal.services.AddressService.ADDRESS_NOT_FOUND;
import static hr.algebra.fishingstore.dal.services.UserService.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {
    public static final String ORDER_NOT_FOUND = "Order Not Found";
    public static final String CART_IS_EMPTY = "Cart is empty";
    public static final String PRODUCT_NOT_FOUND = "Product not found";

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final PayPalOrderService payPalOrderService;
    private final CartSession cartSession;
    private final ProductRepository productRepository;

    public OrderDto.ResponseDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));
        return modelMapper.map(order, OrderDto.ResponseDto.class);
    }

    public List<OrderDto.ResponseDto> getByFilter(String username, LocalDateTime from, LocalDateTime to) {
        Specification<Order> spec = Specification
                .where(OrderSpecification.hasUsername(username))
                .and(OrderSpecification.createdAfter(from))
                .and(OrderSpecification.createdBefore(to));

        return orderRepository.findAll(spec)
                .stream()
                .map(o -> modelMapper.map(o, OrderDto.ResponseDto.class))
                .toList();
    }

    public List<OrderDto.ResponseDto> getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + Role.ADMIN.name()));

        if (isAdmin)
            return orderRepository.findAll()
                    .stream()
                    .map(o -> modelMapper.map(o, OrderDto.ResponseDto.class))
                    .toList();

        return orderRepository.findByUserUsername(username)
                .stream()
                .map(order -> modelMapper.map(order, OrderDto.ResponseDto.class))
                .toList();
    }

    @Transactional
    public OrderDto.ResponseDto create(OrderDto.CreateDto dto) {
        List<CartProduct> cartProducts = getCartProductsFromSession();

        BigDecimal totalPrice = calculateTotalPrice(cartProducts);
        Order order = setupOrderEntity(getAddress(dto), getCurrentUser(), totalPrice);
        Order createdOrder = orderRepository.save(order);

        createProductOrders(cartProducts, createdOrder);

        String approvalUrl = createPayment(dto, createdOrder);
        OrderDto.ResponseDto response = modelMapper.map(createdOrder, OrderDto.ResponseDto.class);
        response.setApprovalUrl(approvalUrl);

        if (dto.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY)
            cartSession.clear();

        return response;
    }

    private String createPayment(OrderDto.CreateDto dto, Order createdOrder) {
        PaymentDto.CreateDto paymentCreateDto = new PaymentDto.CreateDto();
        paymentCreateDto.setOrderId(createdOrder.getId());
        paymentCreateDto.setCurrency(Currency.EUR);
        paymentCreateDto.setPaymentMethod(dto.getPaymentMethod());
        paymentService.create(paymentCreateDto);

        if (dto.getPaymentMethod() == PaymentMethod.PAYPAL)
            return payPalOrderService.initPayPalPayment(createdOrder);

        return null;
    }

    private void createProductOrders(List<CartProduct> cartProducts, Order createdOrder) {
        for (CartProduct item : cartProducts) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(createdOrder);
            productOrder.setProduct(item.getProduct());
            productOrder.setQuantity(item.getQuantity());
            productOrder.setPriceAtPurchase(item.getProduct().getPrice());
            productOrderRepository.save(productOrder);
        }
    }

    private static Order setupOrderEntity(Address address, User user, BigDecimal totalPrice) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAddress(address);
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        return order;
    }

    private static BigDecimal calculateTotalPrice(List<CartProduct> cartProducts) {
        return cartProducts.stream()
                .map(cp -> cp.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cp.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<CartProduct> getCartProductsFromSession() {
        if (cartSession.isEmpty())
            throw new CartEmptyException(CART_IS_EMPTY);

        return cartSession.getCartItems().entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey())
                            .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
                    CartProduct cp = new CartProduct();
                    cp.setProduct(product);
                    cp.setQuantity(entry.getValue());
                    return cp;
                })
                .toList();
    }

    private Address getAddress(OrderDto.CreateDto dto) {
        return addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new EntityNotFoundException(ADDRESS_NOT_FOUND));
    }

    private User getCurrentUser() {
        String currentLoggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(currentLoggedInUsername)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    public void setOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    public OrderDto.ResponseDto update(Long id, OrderDto.EditDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));

        modelMapper.map(dto, order);
        return modelMapper.map(orderRepository.save(order), OrderDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        if (!orderRepository.existsById(id)) return false;

        orderRepository.deleteById(id);
        return true;
    }

    public void cancelOrder(Long orderId) {
        setOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}