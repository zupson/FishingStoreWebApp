package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.OrderDto;
import hr.algebra.fishingstore.dal.services.PayPalOrderService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OrderController.BASE_URL)
@RequiredArgsConstructor
public class OrderController {
    static final String BASE_URL = PathConst.API + PathConst.ORDERS;
    private static final String PAY_PAL_SUCCESS = "/paypal/success";
    private final OrderService orderService;
    private final PayPalOrderService payPalOrderService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<OrderDto.ResponseDto>> getAll() {
        return ResponseEntity
                .ok(orderService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<OrderDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity
                .ok(orderService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.USER_ONLY)
    public ResponseEntity<OrderDto.ResponseDto> create(@Valid @RequestBody OrderDto.CreateDto createDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.create(createDto));
    }

    @PutMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<OrderDto.ResponseDto> update(@PathVariable Long id,
                                                       @Valid @RequestBody OrderDto.EditDto editDto) {
        return ResponseEntity
                .ok(orderService.update(id, editDto));
    }

    @GetMapping(PAY_PAL_SUCCESS)
    public ResponseEntity<OrderDto.ResponseDto> paypalSuccess(@RequestParam String token,
                                                              @RequestParam Long orderId) {
        return ResponseEntity
                .ok(payPalOrderService.confirmPayPalPayment(token, orderId));
    }

    @DeleteMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = orderService.delete(id);
        if (!deleted)
            return ResponseEntity
                    .notFound()
                    .build();

        return ResponseEntity
                .noContent()
                .build();
    }
}