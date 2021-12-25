package ru.volkovd.toDoList.controller;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.volkovd.toDoList.model.Task;
import ru.volkovd.toDoList.service.TaskService;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;


        public TaskController(TaskService taskService) {
            this.taskService = taskService;
        }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("task", taskService.findAll());
        return "task/index";
    }

    @GetMapping("/new")
    public String newTask(@ModelAttribute("task") Task task) {
        return "task/new";
    }

    @PostMapping
    public String create(
            @RequestParam String title) {
        taskService.save(title);
        return "redirect:/task";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Task task,
                       Model model) {
        model.addAttribute("task", task);
        return "task/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Task task,
                       Model model) {
        model.addAttribute("task", task);
        return "task/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Task task,
                         @RequestParam String title) {
        taskService.update(task, title);
        return "redirect:/task";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Task task) {
        taskService.delete(task);
        return "redirect:/task";
    }
}
