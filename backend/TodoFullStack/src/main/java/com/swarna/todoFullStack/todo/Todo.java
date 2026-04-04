package com.swarna.todoFullStack.todo;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="TODO_FULLSTACK", schema = "public")
public class Todo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name = "todo_seq",sequenceName = "todo_seq",allocationSize = 1, schema = "public")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq")
    private Long id;

    private String username;
    private String description;
    private Date targetDate;
    private boolean isDone;
}
