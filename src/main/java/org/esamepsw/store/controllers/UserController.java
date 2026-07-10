package org.esamepsw.store.controllers;


import org.esamepsw.store.entities.User;
import org.esamepsw.store.services.UserService;
import org.esamepsw.store.utilities.exceptions.user.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            User added = userService.addUser(user);
            return new ResponseEntity<>(added, HttpStatus.CREATED);
        }catch (UserAlreadyExistsException e){
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
