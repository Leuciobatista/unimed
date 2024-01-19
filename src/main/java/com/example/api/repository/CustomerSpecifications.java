package com.example.api.repository;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class CustomerSpecifications {

    public static Specification<Customer> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Customer> hasGender(String gender) {
        return (root, query, criteriaBuilder) ->
                gender == null ? null : criteriaBuilder.equal(root.get("gender"), gender);
    }
    public static Specification<Customer> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) return null;
            Join<Customer, Address> addressJoin = root.join("addresses", JoinType.INNER);
            return criteriaBuilder.equal(criteriaBuilder.lower(addressJoin.get("localidade")), city.toLowerCase());
        };
    }

    public static Specification<Customer> hasState(String state) {
        return (root, query, criteriaBuilder) -> {
            if (state == null || state.isEmpty()) return null;
            Join<Customer, Address> addressJoin = root.join("addresses", JoinType.INNER);
            return criteriaBuilder.equal(criteriaBuilder.lower(addressJoin.get("uf")), state.toLowerCase());
        };
    }

}
