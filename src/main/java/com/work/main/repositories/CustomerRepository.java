package com.work.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.work.main.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
