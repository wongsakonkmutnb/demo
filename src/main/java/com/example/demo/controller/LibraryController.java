package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LibraryService;
@Controller
public class LibraryController {

    @Autowired
    private LibraryService libraryService;
    private UserRepository userRepository;
    
    // สมมติให้ User ID = 1 เป็นคนใช้งานระบบอยู่ (เพื่อให้ทดสอบได้ง่าย)
    private final Long CURRENT_USER_ID = 1L; 

    // 1. หน้าแรก (ค้นหาและยืม)
    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String keyword) {
        model.addAttribute("books", libraryService.searchBooks(keyword));
        return "index"; 
    }

@PostMapping("/borrow/{bookId}")
public String borrowBook(@PathVariable Long bookId, Principal principal) {
    // Principal คือข้อมูลของคนที่ Login อยู่
    if (principal == null) return "redirect:/login"; 
    
    User user = userRepository.findByUsername(principal.getName());
    libraryService.borrowBook(user.getId(), bookId);
    return "redirect:/history";
}

    // 2. หน้าประวัติและคืนหนังสือ (เพิ่มใหม่)
@GetMapping("/history")
public String history(Model model, Principal principal) {
    if (principal == null) return "redirect:/login";
    
    User user = userRepository.findByUsername(principal.getName());
    model.addAttribute("records", libraryService.getUserHistory(user.getId()));
    return "history";
}

    @PostMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId) {
        libraryService.returnBook(recordId);
        return "redirect:/history";
    }

    // 3. หน้าจัดการของ Admin
    @GetMapping("/admin/books")
    public String adminBooks(Model model) {
        model.addAttribute("books", libraryService.searchBooks(null));
        model.addAttribute("newBook", new Book());
        return "admin";
    }

    @PostMapping("/admin/addBook")
    public String addBook(@ModelAttribute Book book) {
        libraryService.addBook(book);
        return "redirect:/admin/books";
    }
}