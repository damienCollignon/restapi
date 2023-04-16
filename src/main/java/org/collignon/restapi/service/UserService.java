package org.collignon.restapi.service;

import org.collignon.restapi.exception.NotFoundException;
import org.collignon.restapi.model.User;
import org.collignon.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User insert(User user) {
        return userRepository.insert(user);
    }

    public User update(String id, User user) {
        return Optional.ofNullable(
                userRepository.update(id, user)).orElseThrow(() -> new NotFoundException("User " + id + " not found")
        );
    }

    public User findById(String id) {
        var user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new NotFoundException("User " + id + " not found");
        }
        return user.get();
    }

    public List<User> findAll(User criteria) {
        return userRepository.findAll(criteria);
    }

    public void deleteBydId(String id) {
        userRepository.deleteById(id);
    }
}
