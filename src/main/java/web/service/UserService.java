package web.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;
import web.repository.UserRepositoryImpl;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepositoryImpl userRepositoryImpl;

    @Transactional
    public void save(User user) {
        userRepositoryImpl.save(user);
    }

    public User findById(Long id) {
        return userRepositoryImpl.findById(id);
    }

    public List<User> findAll() {
        return userRepositoryImpl.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepositoryImpl.deleteById(id);
    }

    @Transactional
    public void update(User user) {
        userRepositoryImpl.update(user);
    }
}
