package com.example.api.repository;

import com.example.api.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
	List<Customer> findAllByOrderByNameAsc();

}
