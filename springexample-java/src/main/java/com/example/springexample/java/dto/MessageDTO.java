package com.example.springexample.java.dto;

public record MessageDTO(String message, Object data) {
    public MessageDTO(String message) {
        this(message, null);
    }
}
