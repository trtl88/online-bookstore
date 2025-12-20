package com.orderprocessing.service;

import com.orderprocessing.model.Supplier;
import com.orderprocessing.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    public Supplier getSupplierByEmail(String email) {
        return supplierRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with email: " + email));
    }

    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.findByEmail(supplier.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Supplier with email " + supplier.getEmail() + " already exists");
        }
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier supplier = getSupplierById(id);
        
        supplier.setSupplierName(supplierDetails.getSupplierName());
        supplier.setContactName(supplierDetails.getContactName());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setAddress(supplierDetails.getAddress());
        supplier.setCity(supplierDetails.getCity());
        supplier.setState(supplierDetails.getState());
        supplier.setZipCode(supplierDetails.getZipCode());
        supplier.setCountry(supplierDetails.getCountry());
        
        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplierRepository.delete(supplier);
    }

    public List<Supplier> searchSuppliers(String keyword) {
        return supplierRepository.searchSuppliers(keyword);
    }
}
