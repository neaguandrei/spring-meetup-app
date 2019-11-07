package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User user;

    @BeforeEach
    public void init() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
        user = TestsRepositoryUtil.createUser("login1", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        user.setFirstName("ana");
        user.setLastName("maria");
        userRepository.save(user);
    }

    @Test
    public void assertThatSaveUserIsWorking() {

        assertThat(userRepository.findAll().size()).isEqualTo(1);
        assertThat(userRepository.findAll().get(0).getLogin()).isEqualTo(user.getLogin());
        assertThat(userRepository.findAll().get(0).getPassword()).isEqualTo(user.getPassword());
        assertThat(userRepository.findAll().get(0).getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userRepository.findAll().get(0).getLastName()).isEqualTo(user.getLastName());

    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithValidArguments() {

        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName("ana", "maria");
        assertThat(findUser.get(0).getId()).isEqualTo(userRepository.findAll().get(0).getId());
    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithInvalidFirstName() {

        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName("ana1", "maria");
        assertThat(findUser.size()).isEqualTo(0);
    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithInvalidLastName() {

        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName("ana", "maria1");
        assertThat(findUser.size()).isEqualTo(0);
    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithNullFirstName() {

        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName(null, "maria");
        assertThat(findUser.size()).isEqualTo(0);
    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithNullLastName() {

        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName("ana", null);
        assertThat(findUser.size()).isEqualTo(0);
    }

    @Test
    public void assertThatFindAllByFirstNameAndLastNameIsWorkingWithTwoResults() {

        User user2 = TestsRepositoryUtil.createUser("login2", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        user2.setFirstName("ana");
        user2.setLastName("maria");
        userRepository.save(user2);
        List<UserDTO> findUser = userRepository.findAllByFirstNameAndLastName("ana", "maria");
        assertThat(findUser.size()).isEqualTo(2);
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithValidArgument() {
        List<String> list = userRepository.searchByKeyword("an");
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithInvalidArgument() {
        List<String> list = userRepository.searchByKeyword("lllll");
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithNullArgument() {
        List<String> list = userRepository.searchByKeyword(null);
        assertThat(list.size()).isEqualTo(0);
    }
}
