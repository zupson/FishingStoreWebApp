package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductOrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Product;
import hr.algebra.fishingstore.model.entities.ProductOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ProductOrderDto.ResponseDto getById(Long id) {
        return mapToResponse(productOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Order Not Found")));
    }

    public List<ProductOrderDto.ResponseDto> getAll() {
        return productOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductOrderDto.ResponseDto create(ProductOrderDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantity(dto.getQuantity());
        productOrder.setPriceAtPurchase(product.getPrice());
        productOrder.setOrder(order);
        productOrder.setProduct(product);

        return mapToResponse(productOrderRepository.save(productOrder));
    }

    public ProductOrderDto.ResponseDto update(Long id, ProductOrderDto.EditDto dto) {
        ProductOrder productOrder = productOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Order Not Found"));

        productOrder.setQuantity(dto.getQuantity());

        return mapToResponse(productOrderRepository.save(productOrder));
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