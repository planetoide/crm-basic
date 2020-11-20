package com.work.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.work.main.entities.Customer;
import com.work.main.models.CustomUserDetails;
import com.work.main.models.CustomerCreationRequest;
import com.work.main.models.CustomerUpdateRequest;
import com.work.main.repositories.CustomerRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final StorageService storageService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, StorageService storageService) {
        this.customerRepository = customerRepository;
        this.storageService = storageService;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> create(@Valid @NotNull CustomerCreationRequest creationRequest, CustomUserDetails customUserDetails) {
        Customer customerToSave = Customer.fromCreationRequest(creationRequest);
        customerToSave.setCreatedBy(customUserDetails.getId());
        return Optional.of(customerRepository.save(customerToSave));
    }

    public Optional<Customer> update(Long customerId, CustomerUpdateRequest updateRequest, CustomUserDetails customUserDetails) {
        Customer customerToSave = getCustomerFromDatabase(customerId);
        updateCustomerValues(updateRequest, customerToSave);
        customerToSave.setUpdatedBy(customUserDetails.getId());
        return Optional.ofNullable(customerRepository.save(customerToSave));
    }

    public void delete(Long customerId) {
        Customer customerToSave = getCustomerFromDatabase(customerId);

        customerToSave.setDeleted(true);

        customerRepository.save(customerToSave);
    }

    public String getPhotoUrl(Long customerId) {
        Customer customerFromDatabase = getCustomerFromDatabase(customerId);

        if (customerFromDatabase.getPhotoUrl() == null || customerFromDatabase.getPhotoUrl().isEmpty()) {
            throw new EntityNotFoundException("Customer does not have a photo");
        }


       // return String.format("https://s3.amazonaws.com/theam-crm-images/%s/%s", customerId, customerFromDatabase.getPhotoUrl());
        return String.format("https://s3.amazonaws.com/theam-crm-images/%s/%s", customerId, customerFromDatabase.getPhotoUrl());
    }

    public void saveCustomerPhoto(Long customerId, MultipartFile multipartFile, CustomUserDetails customUserDetails) {
        Customer customerToSave = getCustomerFromDatabase(customerId);

        if (customerToSave.getPhotoUrl() != null && !customerToSave.getPhotoUrl().isEmpty()) {
        	String[] parts = customerToSave.getPhotoUrl().split("/");
            storageService.delete(customerId, parts[parts.length-1]);
        }

        storageService.store(customerId, multipartFile.getOriginalFilename(), multipartFile);

        customerToSave.setPhotoUrl(multipartFile.getOriginalFilename());
        customerToSave.setUpdatedBy(customUserDetails.getId());

        customerRepository.save(customerToSave);

    }

    private Customer getCustomerFromDatabase(Long customerId) throws EntityNotFoundException {
        Optional<Customer> customerFromDatabase = findById(customerId);

        if (!customerFromDatabase.isPresent()) {
            throw new EntityNotFoundException(String.format("Customer with id %d is not in the database", customerId));
        }
        return customerFromDatabase.get();
    }

    private void updateCustomerValues(CustomerUpdateRequest updateRequest, Customer customerToSave) {

        customerToSave.setName(updateRequest.getName());

        customerToSave.setSurname(updateRequest.getSurname());

        customerToSave.setEmail(updateRequest.getEmail());

    }
}
