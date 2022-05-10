package ru.skqwk.elearningsystem.dao;

import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.User;

@Repository
public interface UserDao extends AbstractDao<User> {

    User findUserByLogin(String login);

}
