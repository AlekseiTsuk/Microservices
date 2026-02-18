package Application.microservice.service;

import Application.microservice.model.User;
import Application.microservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
