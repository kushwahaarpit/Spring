
package com.learn.security.springsecurity.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static List<Todo> TODOLIST=
         List.of(new Todo("Course", "Springboot"),
                new Todo("Book", "Doglapan"));


    @GetMapping("/todos")
    public List<Todo> allTodos()
    {
        return TODOLIST;
    }


    @GetMapping("users/{username}/todos")
    public Todo todoForSpecificUser(@PathVariable String username)
    {
        return TODOLIST.get(1);
    }

    @PostMapping("users/{username}/todos")
    public void createTodoForSpecificUser(@PathVariable String username, @RequestBody Todo todo)
    {
        logger.info("Creating a {} for {}",todo,username);
    }
}

record Todo(String username,String description){}
