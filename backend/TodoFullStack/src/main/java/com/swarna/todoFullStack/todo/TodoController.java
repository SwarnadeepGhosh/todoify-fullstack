package com.swarna.todoFullStack.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// @CrossOrigin(origins = "${crossorigins.origin.url}") // Allowing localhost:4200 so that backend can be called by frontendserver
@RestController
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);
    @Autowired
    private TodoService todoService;

    @GetMapping("/users/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username) {
        LOGGER.info("Fetching All todos for particular User " + username);
        return todoService.getAllTodos(username);
        // return todoService.getAllTodos();
        // returns default 200 OK with Todo List
    }

    @GetMapping("/users/{username}/todos/{id}")
    @Transactional(readOnly = true) // True for select, for update/insert, false for default
    @Cacheable("todo-cache")
    public Optional<Todo> getTodo(@PathVariable String username, @PathVariable long id) {
        LOGGER.info("Fetching a todos for particular User " + id);
        return todoService.getTodo(id);
        // returns default 200 OK with one Todo
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    @CacheEvict("todo-cache")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username, @PathVariable long id) {
        Optional<Todo> todo = todoService.deleteById(id);
        if (todo != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
        // returns 204 no Content for successful deletion and returns 404 for not found
    }

    @PutMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String username, @PathVariable long id, @RequestBody Todo todo) {
        Todo todoUpdated = todoService.save(todo, username);
        return new ResponseEntity<Todo>(todoUpdated, HttpStatus.OK);
        // returns 200 OK status with updated Todo
    }

    @PostMapping("/users/{username}/todos")
    public ResponseEntity<Void> createTodo(@PathVariable String username, @RequestBody Todo todo) {
        Todo createdTodo = todoService.save(todo, username);

        // Creating a new URI for the new Todo and returning URL by appending id
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
        System.out.println(uri);
        return ResponseEntity.created(uri).build();
        // returns Location = http://localhost:8080/users/user/todos/4 with response code 201 Created.
    }

}
