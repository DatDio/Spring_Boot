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

    public PageableObject(List<T> data, boolean hasNextPage, long totalPages, int currentPage) {
        this.data = data;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.hasNextPage = hasNextPage;
    }
}