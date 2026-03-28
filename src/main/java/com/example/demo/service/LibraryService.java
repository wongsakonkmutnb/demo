package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowRecord;
import com.example.demo.entity.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowRecordRepository;
import com.example.demo.repository.UserRepository;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private UserRepository userRepository;

    // 1. ค้นหาหนังสือ
    public List<Book> searchBooks(String title) {
        if (title == null || title.isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // 2. ยืมหนังสือ
    public String borrowBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (book != null && user != null && book.getAvailableCopies() > 0) {
            // ลดจำนวนหนังสือ
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);

            // บันทึกประวัติ
            BorrowRecord record = new BorrowRecord();
            record.setUser(user);
            record.setBook(book);
            record.setBorrowDate(LocalDate.now());
            record.setStatus("BORROWED");
            borrowRecordRepository.save(record);

            return "ยืมหนังสือสำเร็จ";
        }
        return "ไม่สามารถยืมได้ (หนังสืออาจหมด)";
    }

    // 3. คืนหนังสือ
    public String returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId).orElse(null);
        if (record != null && record.getStatus().equals("BORROWED")) {
            record.setStatus("RETURNED");
            record.setReturnDate(LocalDate.now());
            borrowRecordRepository.save(record);

            // เพิ่มจำนวนหนังสือกลับ
            Book book = record.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            return "คืนหนังสือสำเร็จ";
        }
        return "ไม่พบข้อมูลการยืม";
    }

    // 4. ดูประวัติ
    public List<BorrowRecord> getUserHistory(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return borrowRecordRepository.findByUser(user);
    }

    // 5. Admin เพิ่มหนังสือ
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
}