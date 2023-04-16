package org.collignon.restapi.service;

import org.collignon.restapi.exception.NotFoundException;
import org.collignon.restapi.model.User;
import org.collignon.restapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void insert() {
        //Given
        var user = new User("test", "Saul", "Goodman");
        Mockito.when(userRepository.insert(user)).thenReturn(user);

        //When
        var result = userService.insert(user);
        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.id(), "test");
        Assertions.assertEquals(result.firstName(), "Saul");
        Assertions.assertEquals(result.lastName(), "Goodman");
    }

    @Test
    public void update() {
        //Given
        var user = new User("test", "Saul", "Goodman");
        Mockito.when(userRepository.update("test", user)).thenReturn(user);

        //When
        var result = userService.update("test", user);
        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.id(), "test");
        Assertions.assertEquals(result.firstName(), "Saul");
        Assertions.assertEquals(result.lastName(), "Goodman");
    }

    @Test
    public void update_NotFound() {
        //Given
        var user = new User("test99", "Saul", "Goodman");
        Mockito.when(userRepository.update("test99", user)).thenReturn(null);

        //When
        Assertions.assertThrows(NotFoundException.class, () -> userService.update("test99", user));
    }

    @Test
    public void findById() {
        //Given
        var user = new User("test", "Saul", "Goodman");
        Mockito.when(userRepository.findById("test")).thenReturn(Optional.of(user));

        //When
        var result = userService.findById("test");
        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.id(), "test");
        Assertions.assertEquals(result.firstName(), "Saul");
        Assertions.assertEquals(result.lastName(), "Goodman");
    }

    @Test
    public void findById_NotFound() {
        //Given
        Mockito.when(userRepository.findById("test99"))
                .thenReturn(Optional.empty());
        //When
        Assertions.assertThrows(NotFoundException.class, () -> userService.findById("test99"));
    }

    @Test
    public void findAll() {
        //Given
        var user = new User("test", "Saul", "Goodman");
        Mockito.when(userRepository.findAll(user)).thenReturn(List.of(user));

        //When
        var result = userService.findAll(user);
        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).id(), "test");
        Assertions.assertEquals(result.get(0).firstName(), "Saul");
        Assertions.assertEquals(result.get(0).lastName(), "Goodman");
    }

    @Test
    public void deleteById() {
        //When
        userService.deleteBydId("test");
        //Then
        Mockito.verify(userRepository).deleteById("test");
    }

}
