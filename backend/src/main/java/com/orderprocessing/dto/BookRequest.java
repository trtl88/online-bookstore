package com.orderprocessing.dto;

import com.orderprocessing.model.Book;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookRequest {
    private String isbn;
    private String title;
    private Long publisherId;
    private List<Long> authorIds;
    private Integer publicationYear;
    private BigDecimal sellingPrice;
    private Book.Category category;
    private Integer quantityInStock;
    private Integer threshold;
}
