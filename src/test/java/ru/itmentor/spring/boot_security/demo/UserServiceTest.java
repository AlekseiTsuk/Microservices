package ru.itmentor.spring.boot_security.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void save_ShouldEncodePasswordAndFetchRoles() {
        Role role = new Role(1L, "ROLE_USER");
        User user = new User();
        user.setPassword("rawPassword");
        user.setRoles(List.of(role));

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        verify(passwordEncoder).encode("rawPassword");
        verify(roleRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void save_ShouldThrowException_WhenRoleNotFound() {
        Role role = new Role(99L, "UNKNOWN");
        User user = new User();
        user.setRoles(List.of(role));

        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.save(user));
    }

    @Test
    void findById_ShouldReturnUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User found = userService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void deleteById_ShouldCallRepository() {
        userService.deleteById(1L);
        verify(userRepository).deleteById(1L);
    }
}
