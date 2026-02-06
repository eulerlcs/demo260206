package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class AddService {

    // Compute sum with range validation; throws IllegalArgumentException on invalid input
    public int add(Integer a, Integer b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Parameters a and b must not be null");
        }
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Parameters must be >= 0");
        }
        if (a > 1000 || b > 1000) {
            throw new IllegalArgumentException("Parameters must be <= 1000");
        }
        return a + b;
    }
}
