package com.example.api.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;

import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Customer>> findAll(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String state,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		List<Customer> customers = service.findAllWithFilters(name, email, gender, city, state, pageable);
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
	}
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
		Customer savedCustomer = service.save(customer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@Valid @PathVariable Long id, @RequestBody Customer customerDetails) {
		Customer updatedCustomer = service.update(id, customerDetails);
		if (updatedCustomer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
		boolean isDeleted = service.delete(id);
		if (!isDeleted) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado com ID: " + id);
		}
		return ResponseEntity.ok("Cliente excluído com sucesso!");
	}


}
