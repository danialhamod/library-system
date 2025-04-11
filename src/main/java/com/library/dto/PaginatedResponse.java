package com.library.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public class PaginatedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private long totalElements;

    public PaginatedResponse(Page<T> page) {
        this.content = page.getContent(); 
        this.pageNumber = page.getNumber();
        this.totalElements = page.getTotalElements();
    }

    // Getters and setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
