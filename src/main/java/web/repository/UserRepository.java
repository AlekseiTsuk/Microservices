package web.repository;

import web.model.User;
import java.util.List;

public interface UserRepository {
    void save(User user);

    List<User> findAll();

    User findById(Long id);

    void update(User user);

    void deleteById(Long id);
}
