package com.example.springexample.java.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springexample.java.model.Example;

@Repository
public interface ExampleRepository extends JpaRepository<Example, UUID> {
}

// package com.example.simple.user.example.repository;
//
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Repository;
//
// import com.example.simple.user.example.model.Example;
//
// @Repository
// public class ExampleRepository {
//
//     @Autowired
//     private ExampleJpaRepository exampleJpaRepository;
//
//     public List<Example> findAll() {
//         return exampleJpaRepository.findAll();
//     }
//
//     public Optional<Example> findById(UUID id) {
//         return exampleJpaRepository.findById(id);
//     }
//
//     public void deleteById(UUID id) {
//         exampleJpaRepository.deleteById(id);
//     }
//
//     public Example insert(String content) {
//         Example example = new Example(content);
//         return exampleJpaRepository.save(example);
//     }
//
//     public Example update(UUID id, String content) {
//         Example example = exampleJpaRepository.findById(id).orElseThrow();
//         example.setContent(content);
//         return exampleJpaRepository.save(example);
//     }
// }
