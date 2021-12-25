package ru.volkovd.toDoList.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import ru.volkovd.toDoList.model.Task;
import ru.volkovd.toDoList.repo.TaskRepo;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private JmsTemplate jmsTemplatePublish;
    private JmsTemplate jmsTemplateQueue;

    public TaskService(TaskRepo taskRepo, JmsTemplate jmsTemplatePublish, JmsTemplate jmsTemplateQueue) {
        this.taskRepo = taskRepo;
        this.jmsTemplatePublish = jmsTemplatePublish;
        this.jmsTemplateQueue = jmsTemplateQueue;
    }

    public void save(String title) {
        Task task = new Task(title);
        taskRepo.save(task);
        jmsTemplatePublish.convertAndSend("journalTopic", "Заметка добавлена: " + title);

        jmsTemplateQueue.send("journalQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setLong("id", task.getId());
                message.setString("title", title);
                return message;
            }
        });
    }

    public Iterable<Task> findAll() {
        return taskRepo.findAll();
    }

    public void update(Task task, String title) {
        task.setTitle(title);
        taskRepo.save(task);
    }

    public void delete(Task task) {
        taskRepo.delete(task);
    }
}
