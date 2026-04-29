package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.ProductOrderDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductOrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Product;
import hr.algebra.fishingstore.model.entities.ProductOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public ProductOrderDto.ResponseDto getById(Long id) {
        ProductOrder productOrder = productOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Order Not Found"));

        return mapToResponse(productOrder);
    }

    public List<ProductOrderDto.ResponseDto> getAll() {
        return productOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductOrderDto.ResponseDto create(ProductOrderDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantity(dto.quantity());
        productOrder.setPriceAtPurchase(dto.priceAtPurchase());
        productOrder.setOrder(order);
        productOrder.setProduct(product);

        ProductOrder createdProductOrder = productOrderRepository.save(productOrder);
        return mapToResponse(createdProductOrder);
    }

    public ProductOrderDto.ResponseDto update(Long id, ProductOrderDto.EditDto dto) {
        ProductOrder productOrder = productOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Order Not Found"));

        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        productOrder.setQuantity(dto.quantity());
        productOrder.setPriceAtPurchase(dto.priceAtPurchase());
        productOrder.setOrder(order);
        productOrder.setProduct(product);

        ProductOrder upratedProductOrder = productOrderRepository.save(productOrder);
        return mapToResponse(upratedProductOrder);
    }

    public boolean delete(Long id) {
        if (!productOrderRepository.existsById(id)) return false;

        productOrderRepository.deleteById(id);
        return true;
    }

    private ProductOrderDto.ResponseDto mapToResponse(ProductOrder productOrder) {
        return new ProductOrderDto.ResponseDto(
                productOrder.getId(),
                productOrder.getQuantity(),
                productOrder.getPriceAtPurchase(),
                productOrder.getOrder().getId(),
                productOrder.getProduct().getId()
        );
    }
}