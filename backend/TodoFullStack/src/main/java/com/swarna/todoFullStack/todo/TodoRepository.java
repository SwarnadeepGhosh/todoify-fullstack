package com.swarna.todoFullStack.todo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    public List<Todo> findByUsername(String username);
}
