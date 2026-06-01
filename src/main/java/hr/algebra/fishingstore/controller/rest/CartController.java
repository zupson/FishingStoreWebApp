package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.CartDto;
import hr.algebra.fishingstore.dal.services.CartService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CartController.BASE_URL)
@RequiredArgsConstructor
public class CartController {
    static final String BASE_URL = PathConst.API + PathConst.CARTS;
    private final CartService cartService;

    @GetMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<CartDto.ResponseDto> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getByUserId(id));
    }
}