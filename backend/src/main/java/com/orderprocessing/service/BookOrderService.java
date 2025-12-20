package com.orderprocessing.service;

import com.orderprocessing.model.Book;
import com.orderprocessing.model.BookOrder;
import com.orderprocessing.repository.BookOrderRepository;
import com.orderprocessing.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookOrderService {
    
    private final BookOrderRepository bookOrderRepository;
    private final BookRepository bookRepository;
    
    public List<BookOrder> findAll() {
        return bookOrderRepository.findAll();
    }
    
    public Optional<BookOrder> findById(Long id) {
        return bookOrderRepository.findById(id);
    }
    
    public List<BookOrder> findPendingOrders() {
        return bookOrderRepository.findByStatus(BookOrder.Status.PENDING);
    }
    
    public List<BookOrder> findByBookId(Long bookId) {
        return bookOrderRepository.findByBook_BookId(bookId);
    }
    
    @Transactional
    public BookOrder placeOrder(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        BookOrder order = new BookOrder();
        order.setBook(book);
        order.setQuantity(quantity);
        order.setStatus(BookOrder.Status.PENDING);
        
        return bookOrderRepository.save(order);
    }
    
    @Transactional
    public BookOrder confirmOrder(Long orderId) {
        BookOrder order = bookOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == BookOrder.Status.CONFIRMED) {
            throw new RuntimeException("Order is already confirmed");
        }
        
        // Add quantity to book stock
        Book book = order.getBook();
        book.setQuantityInStock(book.getQuantityInStock() + order.getQuantity());
        bookRepository.save(book);
        
        // Update order status
        order.confirm();
        return bookOrderRepository.save(order);
    }
    
    public Long getOrderCountByBook(Long bookId) {
        Long count = bookOrderRepository.countOrdersByBookId(bookId);
        return count != null ? count : 0L;
    }
    
    public Long getTotalQuantityOrderedByBook(Long bookId) {
        Long sum = bookOrderRepository.sumQuantityByBookId(bookId);
        return sum != null ? sum : 0L;
    }
}
