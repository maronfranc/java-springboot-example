package com.example.springexample.java.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.springexample.java.dto.ExampleDTO;
import com.example.springexample.java.model.Example;
import com.example.springexample.java.repository.ExampleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExampleController.class)
@DisplayName("ExampleController")
class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExampleRepository exampleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Example example;
    private UUID exampleId;

    @BeforeEach
    void setUp() {
        exampleId = UUID.randomUUID();
        example = new Example(exampleId, "Test content");
    }

    @DisplayName("POST /example")
    class CreateExample {
        @Test
        @DisplayName("should create a new example")
        void testCreate() throws Exception {
            ExampleDTO dto = new ExampleDTO("New content");
            Example savedExample = new Example(UUID.randomUUID(), dto.content());

            when(exampleRepository.save(any(Example.class))).thenReturn(savedExample);

            mockMvc.perform(post("/example")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(jsonPath("$.data.content").value(dto.content()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /example/{id}")
    class UpdateExample {
        @Test
        @DisplayName("should succeed with valid data")
        void testUpdate() throws Exception {
            ExampleDTO dto = new ExampleDTO("Updated content");
            when(exampleRepository.findById(exampleId)).thenReturn(Optional.of(example));
            when(exampleRepository.save(any(Example.class))).thenReturn(example);

            mockMvc.perform(patch("/example/{id}", exampleId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("should throw not found")
        void testUpdateNotFound() throws Exception {
            ExampleDTO dto = new ExampleDTO("Updated content");
            when(exampleRepository.findById(exampleId)).thenReturn(Optional.empty());

            mockMvc.perform(patch("/example/{id}", exampleId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /example/{id}")
    class GetById {
        @Test
        void testGetById() throws Exception {
            when(exampleRepository.findById(exampleId)).thenReturn(Optional.of(example));

            mockMvc.perform(get("/example/{id}", exampleId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.data.id").value(exampleId.toString()))
                    .andExpect(jsonPath("$.data.content").value("Test content"))
                    .andExpect(status().isOk());
        }

        @Test
        void testGetByIdNotFound() throws Exception {
            when(exampleRepository.findById(exampleId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/example/{id}", exampleId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /example")
    class GetMany {
        @Test
        @DisplayName("should return a list with example")
        void testGetAll() throws Exception {
            when(exampleRepository.findAll()).thenReturn(List.of(example));

            mockMvc.perform(get("/example")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(exampleId.toString()))
                    .andExpect(jsonPath("$[0].content").value("Test content"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /example/{id}")
    class DeleteById {
        @Test
        @DisplayName("should delete correct entity")
        void testDelete() throws Exception {
            doNothing().when(exampleRepository).deleteById(exampleId);

            mockMvc.perform(delete("/example/{id}", exampleId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(status().isOk());
        }

        @Nested
        @DisplayName("GET /example/{id} after deletion")
        class GetAfterDeleteById {
            void testGetAfterDeleteNotFound() throws Exception {
                when(exampleRepository.findById(exampleId)).thenReturn(Optional.empty());

                mockMvc.perform(get("/example/{id}", exampleId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").exists())
                        .andExpect(status().isNotFound());
            }
        }
    }

}
