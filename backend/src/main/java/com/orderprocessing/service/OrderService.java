package com.orderprocessing.service;

import com.orderprocessing.dto.OrderItemRequest;
import com.orderprocessing.dto.OrderRequest;
import com.orderprocessing.model.*;
import com.orderprocessing.repository.OrderItemRepository;
import com.orderprocessing.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final InventoryService inventoryService;

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrdersOrderByDateDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdOrderByDateDesc(customerId);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(OrderRequest orderRequest) {
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setShippingCity(orderRequest.getShippingCity());
        order.setShippingState(orderRequest.getShippingState());
        order.setShippingZipCode(orderRequest.getShippingZipCode());
        order.setShippingCountry(orderRequest.getShippingCountry() != null ? 
                orderRequest.getShippingCountry() : "USA");
        order.setTotalAmount(BigDecimal.ZERO);
        
        Order savedOrder = orderRepository.save(order);
        
        // Add order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = addOrderItem(savedOrder, itemRequest);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        
        savedOrder.setTotalAmount(totalAmount);
        return orderRepository.save(savedOrder);
    }

    private OrderItem addOrderItem(Order order, OrderItemRequest itemRequest) {
        Product product = productService.getProductById(itemRequest.getProductId());
        
        // Check and update inventory
        inventoryService.decreaseStock(product.getProductId(), itemRequest.getQuantity());
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setUnitPrice(product.getUnitPrice());
        
        return orderItemRepository.save(orderItem);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        
        // If cancelling, restore inventory
        if (status == Order.OrderStatus.CANCELLED && order.getStatus() != Order.OrderStatus.CANCELLED) {
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.restockProduct(item.getProduct().getProductId(), item.getQuantity());
            }
        }
        
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        
        // Restore inventory before deleting
        if (order.getStatus() != Order.OrderStatus.CANCELLED) {
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.restockProduct(item.getProduct().getProductId(), item.getQuantity());
            }
        }
        
        orderRepository.delete(order);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
}
