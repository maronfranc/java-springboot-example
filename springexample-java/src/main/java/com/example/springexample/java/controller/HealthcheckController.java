package com.example.springexample.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springexample.java.dto.MessageDTO;


@RestController
@RequestMapping("/healthcheck")
public class HealthcheckController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    public ResponseEntity<MessageDTO> ping() {
        MessageDTO msg = new MessageDTO("Pong");
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Custom-Header", "ping")
                .body(msg);
    }

    @GetMapping("/db")
    public ResponseEntity<MessageDTO> pingDatabase() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Custom-Header", "database-ping")
                    .body(new MessageDTO("Database is reachable."));
        } catch (final Exception e) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Custom-Header", "database-ping")
                    .body(new MessageDTO("Database connection failed: " + e.getMessage()));
        }
    }
}
