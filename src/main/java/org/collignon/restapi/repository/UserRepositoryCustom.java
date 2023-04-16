package org.collignon.restapi.repository;

import org.collignon.restapi.model.User;

import java.util.List;

public interface UserRepositoryCustom {
    User update(String id, User user);
    List<User> findAll(User criteria);
}
