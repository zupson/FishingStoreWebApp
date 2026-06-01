package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.dal.repos.OrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductOrderRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Order;
import hr.algebra.fishingstore.model.entities.Product;
import hr.algebra.fishingstore.model.entities.ProductOrder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOrderService {
    public static final String PRODUCT_ORDER_NOT_FOUND = "Product-Order Not Found";
    public static final String ORDER_NOT_FOUND = "Order Not Found";
    public static final String PRODUCT_NOT_FOUND = "Product Not Found";

    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductOrderDto.ResponseDto getById(Long id) {
        ProductOrder productOrder = productOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_ORDER_NOT_FOUND));

        return modelMapper.map(productOrder, ProductOrderDto.ResponseDto.class);
    }

    public List<ProductOrderDto.ResponseDto> getAll() {
        return productOrderRepository.findAll()
                .stream()
                .map(po->modelMapper.map(po, ProductOrderDto.ResponseDto.class))
                .toList();
    }

    public ProductOrderDto.ResponseDto create(ProductOrderDto.CreateDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException(ORDER_NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        ProductOrder productOrder = modelMapper.map(dto, ProductOrder.class);
        productOrder.setPriceAtPurchase(product.getPrice());
        productOrder.setOrder(order);
        productOrder.setProduct(product);

        return modelMapper.map(productOrderRepository.save(productOrder), ProductOrderDto.ResponseDto.class);
    }

    public ProductOrderDto.ResponseDto update(Long id, ProductOrderDto.EditDto dto) {
        ProductOrder productOrder = productOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_ORDER_NOT_FOUND));

        modelMapper.map(dto, productOrder);
        return modelMapper.map(productOrderRepository.save(productOrder), ProductOrderDto.ResponseDto.class);
    }
}