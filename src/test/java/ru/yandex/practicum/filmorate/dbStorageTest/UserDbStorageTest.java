package ru.yandex.practicum.filmorate.dbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanup() {
        jdbcTemplate.update("DELETE FROM USERS");
    }

    @Test
    public void testAddUser() {
        User user = getTestUser();
        User actualUser = userDbStorage.addUser(user);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    public void testGetUserById() {
        User user = getTestUser();
        long id = userDbStorage.addUser(user).getId();
        User actualUser = userDbStorage.getUserById(id);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = new ArrayList<>();
        User user = getTestUser();
        user.setId(1L);
        expectedUsers.add(user);
        user.setId(2L);
        expectedUsers.add(user);
        user.setId(3L);
        expectedUsers.add(user);

        userDbStorage.addUser(user);
        userDbStorage.addUser(user);
        userDbStorage.addUser(user);

        List<User> actualUsers = userDbStorage.getAllUsers();
        Assertions.assertEquals(3, actualUsers.size());
        Assertions.assertTrue(actualUsers.containsAll(expectedUsers));
    }

    @Test
    public void testUpdateUser() {
        User user = getTestUser();
        long id = userDbStorage.addUser(user).getId();
        User actualUser = userDbStorage.getUserById(id);
        userDbStorage.updateUser(user);
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    public void testDeleteUser() {
        User user = getTestUser();
        userDbStorage.addUser(user);
        userDbStorage.deleteUser(user.getId());
        Assertions.assertEquals(0, userDbStorage.getAllUsers().size());
    }

    private User getTestUser() {
        User user = new User();
        user.setName("TestName");
        user.setLogin("TestLogin");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.now());
        return user;
    }
}