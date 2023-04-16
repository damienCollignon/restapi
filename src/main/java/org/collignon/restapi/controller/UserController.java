package org.collignon.restapi.controller;

import org.collignon.restapi.model.User;
import org.collignon.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User insert(@Validated @RequestBody User user) {
        return userService.insert(user);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User update(@PathVariable String id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") String id) {
        return userService.findById(id);
    }


    @GetMapping
    public List<User> findAll(User criteria) {
        return userService.findAll(criteria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        userService.deleteBydId(id);
    }
}
