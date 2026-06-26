package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.repos.*;
import hr.algebra.fishingstore.dal.services.payment.PayPalService;
import hr.algebra.fishingstore.dal.specifications.OrderSpecification;
import hr.algebra.fishingstore.exceptions.CartEmptyException;
import hr.algebra.fishingstore.model.entities.*;
import hr.algebra.fishingstore.model.enums.*;
import hr.algebra.fishingstore.session.CartSession;
import hr.algebra.fishingstore.utilities.PathConst;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static hr.algebra.fishingstore.dal.services.AddressService.ADDRESS_NOT_FOUND;
import static hr.algebra.fishingstore.dal.services.PaymentService.PAYMENT_NOT_FOUND;
import static hr.algebra.fishingstore.dal.services.UserService.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {
    public static final String ORDER_NOT_FOUND = "Order Not Found";
    public static final String CART_IS_EMPTY = "Cart is empty";

    private static final String ORDER_ID_Q_PARAM = "?orderId=";
    private static final String SUCCESS_URL = PathConst.MVC + PathConst.ORDERS + "/paypal/success" + ORDER_ID_Q_PARAM;
    private static final String CANCEL_URL = PathConst.MVC + PathConst.ORDERS + "/paypal/cancel";
    private static final String PAL_ORDER_CREATION_FAILED = "PayPal order creation failed";

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final ProductOrderRepository productOrderRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final PayPalService payPalService;
    private final CartSession cartSession;
    private final ProductRepository productRepository;

    @Value("${app.base-url}")
    private String baseUrl;

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
            return initPayPalPayment(createdOrder);

        return null;
    }

    private String initPayPalPayment(Order createdOrder) {
        try {
            return payPalService.createOrder(
                    createdOrder.getTotalPrice(),
                    createdOrder.getId(),
                    baseUrl + SUCCESS_URL + createdOrder.getId(),
                    baseUrl + CANCEL_URL+ ORDER_ID_Q_PARAM +createdOrder.getId()
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(PAL_ORDER_CREATION_FAILED, e);
        }
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
                            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
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

    public OrderDto.ResponseDto confirmPayPalPayment(String payPalOrderId, Long orderId) {
        try {
            boolean isSuccessCaptured = payPalService.captureOrder(payPalOrderId);
            if (isSuccessCaptured) {
                Payment payment = getPayment(orderId);
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setPaypalTransactionId(payPalOrderId);
                payment.setPaidAt(LocalDateTime.now());
                paymentRepository.save(payment);
                
                setOrderStatus(orderId, OrderStatus.CONFIRMED);
                cartSession.clear();
            }

            return getById(orderId);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void setOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private Payment getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(PAYMENT_NOT_FOUND));
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