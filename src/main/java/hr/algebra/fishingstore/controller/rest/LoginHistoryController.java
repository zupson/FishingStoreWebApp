package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.LoginHistoryDto;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(LoginHistoryController.BASE_URL)
@RequiredArgsConstructor
public class LoginHistoryController {
    static final String BASE_URL = PathConst.API + PathConst.LOGIN_HISTORIES;

    private final LoginHistoryService loginHistoryService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<LoginHistoryDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(loginHistoryService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<LoginHistoryDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loginHistoryService.getById(id));
    }
}