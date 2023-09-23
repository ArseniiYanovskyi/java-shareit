package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DisplayName("UserRepository")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private User fourthUser;
    private User fifthUser;
    private User sixthUser;

    @BeforeAll
    void beforeEach() {
        firstUser = userRepository.save(new User(1L, "FirstUser", "FirstUser@somemail.com"));
        secondUser = userRepository.save(new User(2L, "SecondUser", "SecondUser@somemail.com"));
        thirdUser = userRepository.save(new User(3L, "ThirdUser", "ThirdUser@somemail.com"));
        fourthUser = userRepository.save(new User(4L, "FourthUser", "FourthUser@somemail.com"));
        fifthUser = userRepository.save(new User(5L, "FifthUser", "FifthUser@somemail.com"));
        sixthUser = userRepository.save(new User(6L, "SixthUser", "SixthUser@somemail.com"));
    }

    @Test
    @Order(value = 1)
    @DisplayName("1 - should search by email.")
    void shouldSearchByEmail() {
        List<User> expectingResult = new ArrayList<>();
        expectingResult.add(fourthUser);
        expectingResult.add(fifthUser);
        expectingResult.add(sixthUser);

        List<User> actualResult = userRepository.findByEmailContainingIgnoreCase("thu");

        assertThat(expectingResult.size(), equalTo(actualResult.size()));

        assertThat(expectingResult.get(0).getId(), equalTo(actualResult.get(0).getId()));
        assertThat(expectingResult.get(0).getName(), equalTo(actualResult.get(0).getName()));
        assertThat(expectingResult.get(0).getEmail(), equalTo(actualResult.get(0).getEmail()));

        assertThat(expectingResult.get(1).getId(), equalTo(actualResult.get(1).getId()));
        assertThat(expectingResult.get(1).getName(), equalTo(actualResult.get(1).getName()));
        assertThat(expectingResult.get(1).getEmail(), equalTo(actualResult.get(1).getEmail()));

        assertThat(expectingResult.get(2).getId(), equalTo(actualResult.get(2).getId()));
        assertThat(expectingResult.get(2).getName(), equalTo(actualResult.get(2).getName()));
        assertThat(expectingResult.get(2).getEmail(), equalTo(actualResult.get(2).getEmail()));
    }

}
