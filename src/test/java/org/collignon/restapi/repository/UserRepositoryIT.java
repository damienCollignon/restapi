package org.collignon.restapi.repository;

import org.collignon.restapi.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
        userRepository.insert(new User("test", "John", "Doe"));
        userRepository.insert(new User("test2", "Kenny", "Powers"));
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
    }

    @Test
    public void findAll() {
        var users = userRepository.findAll(new User(null, null, null));
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void findAllByFirstName() {
        var users = userRepository.findAll(new User(null, "joh", null));
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("test", users.get(0).id());
    }

    @Test
    public void findAllByLastName() {
        var users = userRepository.findAll(new User(null, null, "wers"));
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("test2", users.get(0).id());
    }

    @Test
    public void findAllByFirstNameAndLastName() {
        var users = userRepository.findAll(new User(null, "ohn", "Doe"));
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals("test", users.get(0).id());
    }

    @Test
    public void update() {
        userRepository.update("test", new User(null, "Michel", "Lenul"));

        var user1 = userRepository.findById("test").orElse(null);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Michel", user1.firstName());
        Assertions.assertEquals("Lenul", user1.lastName());

        var user2 = userRepository.findById("test2").orElse(null);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals("Kenny", user2.firstName());
        Assertions.assertEquals("Powers", user2.lastName());
    }

    @Test
    public void updateFirstName() {
        userRepository.update("test", new User(null, "Michel", null));

        var user1 = userRepository.findById("test").orElse(null);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Michel", user1.firstName());
        Assertions.assertEquals("Doe", user1.lastName());

        var user2 = userRepository.findById("test2").orElse(null);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals("Kenny", user2.firstName());
        Assertions.assertEquals("Powers", user2.lastName());
    }

    @Test
    public void updateLastName() {
        userRepository.update("test", new User(null, null, "Lenul"));

        var user1 = userRepository.findById("test").orElse(null);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("John", user1.firstName());
        Assertions.assertEquals("Lenul", user1.lastName());

        var user2 = userRepository.findById("test2").orElse(null);
        Assertions.assertNotNull(user2);
        Assertions.assertEquals("Kenny", user2.firstName());
        Assertions.assertEquals("Powers", user2.lastName());
    }
}
