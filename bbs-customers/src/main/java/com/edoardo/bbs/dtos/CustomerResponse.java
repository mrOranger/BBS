package com.edoardo.bbs.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data  @Builder
public class CustomerResponse {
    private List<CustomerDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
