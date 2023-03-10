package edu.AnastasiiaTkachuk.dao;

import edu.AnastasiiaTkachuk.entity.Gender;
import edu.AnastasiiaTkachuk.entity.Role;
import edu.AnastasiiaTkachuk.entity.User;
import edu.AnastasiiaTkachuk.integration.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends IntegrationTestBase {
    private final UserDao userDao = UserDao.getInstance();

    @Test
    void findAll() {
        User user1 = userDao.save(getUser("test1@gmail.com"));
        User user2 = userDao.save(getUser("test2@gmail.com"));
        User user3 = userDao.save(getUser("test3@gmail.com"));

        List<User> actualResult = userDao.findAll();
        assertThat(actualResult).hasSize(3);
        List<Integer> userIds = actualResult.stream()
                .map(User::getId)
                .toList();
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findById() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void save() {
        User user = getUser("test@gmail.com");

        User actualResult = userDao.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findByEmailAndPassword() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }
    @Test
    void shouldNotFindByEmailAndPasswordIfUserDoesNotExist(){
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword("dummy", "123");

        assertThat(actualResult).isEmpty();

    }

    @Test
    void deleteExistingEntity() {
        User user = userDao.save(getUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(user.getId());

        assertTrue(actualResult);
    }
    @Test
    void deleteNotExistingEntity() {
        User user = userDao.save(getUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(123);

        assertFalse(actualResult);
    }

    @Test
    void update() {
        User user = getUser("test@gmail.com");
        userDao.save(user);
        user.setName("Ivan-updated");
        user.setPassword("new password");

        userDao.update(user);

        User updatedUser = userDao.findById(user.getId()).get();
        assertThat(updatedUser).isEqualTo(user);
    }
    private static User getUser(String email) {
        return User.builder()
                .name("Ivan")
                .gender(Gender.MALE)
                .role(Role.USER)
                .email(email)
                .password("123")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
    }
}