package ru.volkovd.toDoList.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volkovd.toDoList.model.Task;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
