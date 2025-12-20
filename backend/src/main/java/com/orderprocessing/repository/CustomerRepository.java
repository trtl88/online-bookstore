package com.orderprocessing.repository;

import com.orderprocessing.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
    
    List<Customer> findByCity(String city);
    
    List<Customer> findByState(String state);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchCustomers(String keyword);
}
