package com.library.admin.payload;

import lombok.Data;

@Data
public class LoanCreateRequest {
    private Long userId;
    private String isbn;
}
