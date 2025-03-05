package com.example.learn_spring_boot.Utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageableObject<T> {
    private List<T> data;
    private boolean hasNextPage;
    private long totalPages;
    private int currentPage;

    public PageableObject(Page<T> page) {
        this.data = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber() + 1;
        this.hasNextPage = page.hasNext(); // Sử dụng hasNext() thay vì page.gethasNextPage()
    }
}