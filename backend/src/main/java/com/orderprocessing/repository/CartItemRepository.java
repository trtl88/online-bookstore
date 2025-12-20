package com.orderprocessing.repository;

import com.orderprocessing.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_CartId(Long cartId);
    Optional<CartItem> findByCart_CartIdAndBook_BookId(Long cartId, Long bookId);
    void deleteByCart_CartId(Long cartId);
}
