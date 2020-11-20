package com.work.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.work.main.entities.Customer;
import com.work.main.exceptions.ExceptionHandlerController;
import com.work.main.models.CustomUserDetails;
import com.work.main.models.CustomerCreationRequest;
import com.work.main.models.CustomerUpdateRequest;
import com.work.main.services.CustomerService;

import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://stark-coast-58195.herokuapp.com")
@RequestMapping("/customers")
public class CustomerController extends ExceptionHandlerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @ApiOperation(value = "Obtiene todos los clientes", notes = "El valor del header usa Bearer")
    public List<Customer> findAll(@RequestHeader(value="x-access-token") String headerStr) {
        return customerService.findAll();
    }

    @GetMapping("/{customerId}")
    @ApiOperation(value = "Obtiene el cliente por id", notes = "El valor del header usa Bearer")
    public ResponseEntity<Customer> findById(@RequestHeader(value="x-access-token") String headerStr, @PathVariable Long customerId) {
        return customerService.findById(customerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @NotNull @RequestBody CustomerCreationRequest creationRequest, Authentication authentication) {
        return customerService.create(creationRequest, (CustomUserDetails) authentication.getPrincipal())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable Long customerId, @Valid @NotNull @RequestBody CustomerUpdateRequest updateRequest, Authentication authentication) {
        return customerService.update(customerId, updateRequest, (CustomUserDetails) authentication.getPrincipal())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long customerId) {
        customerService.delete(customerId);
    }

    @GetMapping("/{customerId}/photo")
    public String getPhotoUrl(@PathVariable("customerId") Long customerId) {
        return customerService.getPhotoUrl(customerId);
    }
    
    /*@PostMapping("/{customerId}/photo")
    @ResponseStatus(value = HttpStatus.OK)
    public void handleFileUpload(@PathVariable("customerId") Long customerId, @RequestParam("file") MultipartFile file, Authentication authentication) {
        customerService.saveCustomerPhoto(customerId, file, (CustomUserDetails) authentication.getPrincipal());
    }*/
    
    @PostMapping("/{customerId}/photo")
    @ResponseStatus(value = HttpStatus.OK)
    public void handleFileUpload(@PathVariable("customerId") Long customerId, @RequestParam("image") MultipartFile multipartFile, Authentication authentication) {
        customerService.saveCustomerPhoto(customerId, multipartFile, (CustomUserDetails) authentication.getPrincipal());
    }
    
    @PostMapping("/foto")
    public String handleFileUploaddos(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        //customerService.saveCustomerPhoto(customerId, file, (CustomUserDetails) authentication.getPrincipal());
    	String fileName = multipartFile.getOriginalFilename();
    	return fileName;
    }
}
