package org.collignon.restapi.repository;

import org.collignon.restapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public User update(String id, User user) {
        var criteria = Criteria.where("_id").is(id);
        var update = new Update();
        if (hasText(user.firstName())) {
            update.set("firstName", user.firstName());
        }
        if (hasText(user.lastName())) {
            update.set("lastName", user.lastName());
        }
        return mongoTemplate.findAndModify(
                Query.query(criteria),
                update,
                FindAndModifyOptions.options().returnNew(true),
                User.class
        );
    }

    @Override
    public List<User> findAll(User criteria) {
        var query = new Criteria();
        if (hasText(criteria.firstName())) {
            query.and("firstName").regex(".*" + criteria.firstName() + ".*", "i");
        }
        if (hasText(criteria.lastName())) {
            query.and("lastName").regex(".*" + criteria.lastName() + ".*", "i");
        }
        return mongoTemplate.find(Query.query(query), User.class);
    }
}
