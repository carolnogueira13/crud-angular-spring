package com.example.crudspring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudspring.model.Course;
import com.example.crudspring.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<Course> list(){
        return courseRepository.findAll();
    }

    @GetMapping("/{id}") // PathVarible - variavel no caminho 
    public ResponseEntity<Course> findById(@PathVariable Long id){
        return courseRepository.findById(id)
            .map(recordFound -> ResponseEntity.ok().body(recordFound))
            .orElse(ResponseEntity.notFound().build());
        
        // O que esse return significa: Esse findById ele retorna um Optional, pq ele pode encontra um curso ou não procurando pelo Id, 
        // então podemos tratar esse retorno do findById, utilizando o ResponseEntity e a programação funcional. Caso positivo o 
        // retorno irá para função map (que é executada em todos os termos) que irá retornar um ReponseEntity OK e no body o courso, se não irá para o orElse que irá retornar noFound no ResponseEntity. 
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Course create(@RequestBody Course course){
        // System.out.println(course.getName());
        return courseRepository.save(course);
        // return ResponseEntity.status(HttpStatus.CREATED).body(courseRepository.save(course));    
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course course){
        return courseRepository.findById(id)
            .map(recordFound -> {
                recordFound.setName(course.getName());
                recordFound.setCategory(course.getCategory());
                Course updated = courseRepository.save(recordFound);
                return ResponseEntity.ok().body(updated);
            }
            )
            .orElse(ResponseEntity.notFound().build());

    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return courseRepository.findById(id)
            .map(recordFound -> {
                courseRepository.deleteById(id);
                return ResponseEntity.noContent().<Void>build();
            }
            )
            .orElse(ResponseEntity.notFound().build());
    }
}
