package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    // ใช้สำหรับค้นหาหนังสือด้วยชื่อ (มีคำไหนก็เจอ)
    List<Book> findByTitleContainingIgnoreCase(String title);
}
