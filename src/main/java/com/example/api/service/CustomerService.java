package com.example.api.service;

import java.util.List;
import java.util.Optional;

import com.example.api.domain.Address;
import com.example.api.domain.AddressResponse;
import com.example.api.exception.InvalidCepException;
import com.example.api.repository.CustomerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import static com.example.api.service.ViaCepService.getAddressFromCep;

@Service
public class CustomerService {

	private final CustomerRepository repository;

	@Autowired
	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll() {
		return repository.findAllByOrderByNameAsc();
	}

	public Optional<Customer> findById(Long id) {
		return repository.findById(id);
	}

	public List<Customer> findAllWithFilters(String name, String email, String gender, String city, String state, Pageable pageable) {
		Specification<Customer> spec = Specification.where(CustomerSpecifications.hasName(name))
				.and(CustomerSpecifications.hasEmail(email))
				.and(CustomerSpecifications.hasGender(gender))
				.and(CustomerSpecifications.hasCity(city))
				.and(CustomerSpecifications.hasState(state));

		Page<Customer> page = repository.findAll(spec, pageable);
		return page.getContent();
	}

	public Customer save(Customer customer) {
		if (customer.getAddresses() != null) {
			for (Address address : customer.getAddresses()) {
				AddressResponse filledAddressResponse = getAddressFromCep(address.getCep());
				if (filledAddressResponse.getAddress() != null) {
					Address filledAddress = filledAddressResponse.getAddress();
					address.setLogradouro(filledAddress.getLogradouro());
					address.setComplemento(filledAddress.getComplemento());
					address.setBairro(filledAddress.getBairro());
					address.setLocalidade(filledAddress.getLocalidade());
					address.setUf(filledAddress.getUf());
					address.setCep(filledAddress.getCep());
					address.setIbge(filledAddress.getIbge());
					address.setGia(filledAddress.getGia());
					address.setDdd(filledAddress.getDdd());
					address.setSiafi(filledAddress.getSiafi());
					address.setCustomer(customer);
				} else {
					throw new InvalidCepException(filledAddressResponse.getMessage());
				}
			}
		}
		return repository.save(customer);
	}



	public Customer update(Long id, Customer customerDetails) {
		return repository.findById(id)
				.map(customer -> {
					customer.setName(customerDetails.getName());
					customer.setEmail(customerDetails.getEmail());
					customer.setGender(customerDetails.getGender());

					if (customerDetails.getAddresses() != null) {
						customer.getAddresses().clear();

						for (Address address : customerDetails.getAddresses()) {
							address.setCustomer(customer);
							customer.getAddresses().add(address);
						}
					}
					return repository.save(customer);
				}).orElse(null);
	}


	public boolean delete(Long id) {
		return repository.findById(id)
				.map(customer -> {
					repository.delete(customer);
					return true;
				}).orElse(false);
	}

}
