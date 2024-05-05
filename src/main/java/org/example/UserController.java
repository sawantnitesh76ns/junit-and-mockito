package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{userEmail}")
    public User getUserById(@PathVariable String userEmail) {
        return userRepository.findById(userEmail).get();
    }


    @PostMapping("/users")
    public User postUsers(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @PutMapping("/users/{userEmail}")
    public User putUser(@PathVariable String userEmail, @RequestBody User user) throws Exception {
        Optional<User> mayBeUser = userRepository.findById(userEmail);
        if(mayBeUser.isPresent()) {
            User existingUser = mayBeUser.get();
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            userRepository.save(user);
            return user;
        } else {
            throw new Exception("User with email: " + userEmail + " does not exists.");
        }
    }

    @DeleteMapping("/users/{userEmail}")
    public void deleteUser(@PathVariable String userEmail) {
        Optional<User> mayBeUser = userRepository.findById(userEmail);
        if(mayBeUser.isPresent()) {
            userRepository.deleteById(userEmail);
        } else {
            throw new NoSuchElementException("User with email: " + userEmail + " does not exists.");
        }
    }

}
