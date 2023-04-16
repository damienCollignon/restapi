package org.collignon.restapi.controller;

import com.mongodb.assertions.Assertions;
import org.collignon.restapi.model.User;
import org.collignon.restapi.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

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
    public void insert() throws Exception {
        mvc.perform(
                        post("/users").content(
                                """
                                        {
                                            "id":"BCS",
                                            "firstName": "Saul",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is("BCS")))
                .andExpect(jsonPath("firstName", is("Saul")))
                .andExpect(jsonPath("lastName", is("Goodman")));
    }

    @Test
    public void insert_WithoutId() throws Exception {
        mvc.perform(
                        post("/users").content(
                                """
                                        {
                                            "firstName": "Saul",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.notNullValue()))
                .andExpect(jsonPath("firstName", is("Saul")))
                .andExpect(jsonPath("lastName", is("Goodman")));
    }

    @Test
    public void insert_DuplicateKey() throws Exception {
        mvc.perform(
                        post("/users").content(
                                """
                                        {
                                            "id": "test",
                                            "firstName": "Saul",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", Matchers.notNullValue()))
                .andExpect(jsonPath("code", is("DUPLICATE_KEY")));
    }

    @Test
    public void insert_ValidationOneField() throws Exception {
        mvc.perform(
                        post("/users").content(
                                """
                                        {
                                            "id": "test",
                                            "firstName": "",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fields", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("code", is("MISSING_FIELD")));
    }

    @Test
    public void insert_ValidationTwoFields() throws Exception {
        mvc.perform(
                        post("/users").content(
                                """
                                        {
                                            "id": "test",
                                            "firstName": ""
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("fields", Matchers.aMapWithSize(2)))
                .andExpect(jsonPath("code", is("MISSING_FIELD")));
    }

    @Test
    public void update() throws Exception {
        mvc.perform(
                        post("/users/test").content(
                                """
                                        {
                                            "firstName": "Saul",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is("test")))
                .andExpect(jsonPath("firstName", is("Saul")))
                .andExpect(jsonPath("lastName", is("Goodman")));
    }

    @Test
    public void update_Firstname() throws Exception {
        mvc.perform(
                        post("/users/test").content(
                                """
                                        {
                                            "firstName": "Saul"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is("test")))
                .andExpect(jsonPath("firstName", is("Saul")))
                .andExpect(jsonPath("lastName", is("Doe")));
    }

    @Test
    public void update_LastName() throws Exception {
        mvc.perform(
                        post("/users/test").content(
                                """
                                        {
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is("test")))
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("lastName", is("Goodman")));
    }

    @Test
    public void update_NotFound() throws Exception {
        mvc.perform(
                        post("/users/test99").content(
                                """
                                        {
                                            "firstName": "Saul",
                                            "lastName": "Goodman"
                                        }
                                        """
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", is("User test99 not found")))
                .andExpect(jsonPath("code", is("NOT_FOUND")));
    }

    @Test
    public void findById() throws Exception {
        mvc.perform(get("/users/test").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is("test")))
                .andExpect(jsonPath("firstName", is("John")))
                .andExpect(jsonPath("lastName", is("Doe")));
    }

    @Test
    public void findById_NotFound() throws Exception {
        mvc.perform(get("/users/test99").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message", is("User test99 not found")))
                .andExpect(jsonPath("code", is("NOT_FOUND")));
    }

    @Test
    public void findAll() throws Exception {
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",Matchers.hasSize(2)));
    }

    @Test
    public void findAll_ByFirstName() throws Exception {
        mvc.perform(get("/users?firstName=John").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("test")));
    }

    @Test
    public void findAll_ByFirstNameAndLastName() throws Exception {
        mvc.perform(get("/users?firstName=Joh&lastName=Do").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("test")));
    }

    @Test
    public void findAll_EmptyList() throws Exception {
        mvc.perform(get("/users?firstName=Emile").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",Matchers.hasSize(0)));
    }

    @Test
    public void deleteById() throws Exception {
        mvc.perform(delete("/users/test"))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(userRepository.findById("test").isEmpty());
    }

}
