package com.orderprocessing.service;

import com.orderprocessing.model.Author;
import com.orderprocessing.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    
    private final AuthorRepository authorRepository;
    
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
    
    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }
    
    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }
    
    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }
    
    @Transactional
    public Author findOrCreate(String name) {
        return authorRepository.findByName(name)
            .orElseGet(() -> {
                Author author = new Author();
                author.setName(name);
                return authorRepository.save(author);
            });
    }
    
    @Transactional
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
