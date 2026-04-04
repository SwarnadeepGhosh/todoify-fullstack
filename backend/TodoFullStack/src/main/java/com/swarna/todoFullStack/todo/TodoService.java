package com.swarna.todoFullStack.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    // private List<Todo> todos = new ArrayList<>();
    // private static int idCounter = 0;
    // static {
    // 	todos.add(new Todo(++idCounter, "user", "My first Todo", new Date(), false));
    // 	todos.add(new Todo(++idCounter, "user", "Spring Boot microservices", new Date(), true));
    // 	todos.add(new Todo(++idCounter, "user", "Angular", new Date(), false));
    // }

    public List<Todo> getAllTodos(String username) {
        // List<Todo> todos = new ArrayList<>();
        return todoRepository.findByUsername(username);

        // todoRepository.findAll().forEach(t -> todos.add(t));;
        // return todos;
    }

    public Optional<Todo> getTodo(long id) {
        return todoRepository.findById(id);
    }

    public Todo save(Todo todo, String username) {
        todo.setUsername(username);
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo;
    }

    public Optional<Todo> deleteById(long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        todoRepository.deleteById(id);
        return todo;
    }

}
