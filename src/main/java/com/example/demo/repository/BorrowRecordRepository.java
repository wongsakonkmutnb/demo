package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.BorrowRecord;
import com.example.demo.entity.User;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // ดูประวัติการยืมของ User แต่ละคน
    List<BorrowRecord> findByUser(User user);
}