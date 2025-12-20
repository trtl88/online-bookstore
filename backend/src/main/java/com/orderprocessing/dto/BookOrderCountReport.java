package com.orderprocessing.dto;

import lombok.Data;

@Data
public class BookOrderCountReport {
    private Long bookId;
    private String isbn;
    private String title;
    private Long timesOrdered;
    private Long totalQuantityOrdered;
}
