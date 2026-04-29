package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.LoginHistoryDto;
import hr.algebra.fishingstore.dal.services.LoginHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login-histories")

public class LoginHistoryController {
    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<LoginHistoryDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(loginHistoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoginHistoryDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loginHistoryService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<LoginHistoryDto.ResponseDto> create(@Valid @RequestBody LoginHistoryDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loginHistoryService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LoginHistoryDto.ResponseDto> update(@PathVariable Long id,@Valid @RequestBody LoginHistoryDto.UpdateDto updateDto) {
        return ResponseEntity.ok(loginHistoryService.update(id,updateDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = loginHistoryService.delete(id);
        if(!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}