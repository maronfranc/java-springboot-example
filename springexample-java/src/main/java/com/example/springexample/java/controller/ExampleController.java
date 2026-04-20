package com.example.springexample.java.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springexample.java.dto.ExampleDTO;
import com.example.springexample.java.dto.MessageDTO;
import com.example.springexample.java.model.Example;
import com.example.springexample.java.repository.ExampleRepository;

@RestController
@RequestMapping("/example")
public class ExampleController {
    @Autowired
    private ExampleRepository exampleRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Example> getAll() {
        return exampleRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessageDTO> getById(@PathVariable UUID id) {
        System.out.println(id);
        Example example = exampleRepository.findById(id).orElse(null);
        if (example == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageDTO("Example not found"));
        }

        return ResponseEntity.ok(new MessageDTO("Example found", example));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageDTO> create(@RequestBody ExampleDTO dto) {
        Example example = exampleRepository.save(new Example(null, dto.content()));

        return ResponseEntity.ok(new MessageDTO(
                "Example created successfully",
                example));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<MessageDTO> update(
            @PathVariable UUID id,
            @RequestBody ExampleDTO dto) {
        Example example = exampleRepository.findById(id).orElse(null);
        if (example == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageDTO("Example not found"));
        }

        example.setContent(dto.content());
        exampleRepository.save(example);

        return ResponseEntity.ok(new MessageDTO("Example updated successfully"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageDTO> delete(@PathVariable UUID id) {
        exampleRepository.deleteById(id);
        return ResponseEntity.ok(new MessageDTO("Example deleted"));
    }
}
