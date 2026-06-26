package hr.algebra.fishingstore.dal.specifications;

import hr.algebra.fishingstore.model.entities.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {

    public static final String USER = "user";
    public static final String USERNAME = "username";
    public static final String CREATED_AT = "createdAt";

    public static Specification<Order> hasUsername(String username) {
        return (orderEntity, query, criteriaBuilder) -> username == null || username.isBlank() ? null :
                criteriaBuilder.like(criteriaBuilder.lower(orderEntity.get(USER).get(USERNAME)), "%" + username.toLowerCase() + "%");
    }

    public static Specification<Order> createdAfter(LocalDateTime from) {
        return (orderEntity, query, criteriaBuilder) -> from == null ? null :
                criteriaBuilder.greaterThanOrEqualTo(orderEntity.get(CREATED_AT), from);
    }

    public static Specification<Order> createdBefore(LocalDateTime to) {
        return (orderEntity, query, criteriaBuilder) -> to == null ? null :
                criteriaBuilder.lessThanOrEqualTo(orderEntity.get(CREATED_AT), to);
    }
}