package hr.algebra.fishingstore.dal.dtos;

import java.time.LocalDateTime;

public class CartDto{
    public record ResponseDto(
            Long id,
            LocalDateTime updatedAt,
            Long userId
    ){}
}