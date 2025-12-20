package com.orderprocessing.service;

import com.orderprocessing.model.Publisher;
import com.orderprocessing.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {
    
    private final PublisherRepository publisherRepository;
    
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }
    
    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }
    
    @Transactional
    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }
    
    @Transactional
    public Publisher update(Long id, Publisher publisherDetails) {
        Publisher publisher = publisherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        
        publisher.setName(publisherDetails.getName());
        publisher.setAddress(publisherDetails.getAddress());
        publisher.setPhone(publisherDetails.getPhone());
        
        return publisherRepository.save(publisher);
    }
    
    @Transactional
    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }
}
