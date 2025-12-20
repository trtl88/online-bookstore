package com.orderprocessing.repository;

import com.orderprocessing.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    Optional<Supplier> findByEmail(String email);
    
    List<Supplier> findBySupplierNameContainingIgnoreCase(String supplierName);
    
    List<Supplier> findByCity(String city);
    
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.supplierName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(s.contactName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Supplier> searchSuppliers(String keyword);
}
