package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.config.Constants;
import com.cegeka.academy.domain.Address;
import com.cegeka.academy.domain.Category;
import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.AddressRepository;
import com.cegeka.academy.repository.CategoryRepository;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.dto.UserDTO;
import com.cegeka.academy.service.util.RandomUtil;
import com.cegeka.academy.web.rest.errors.InvalidArgumentsException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Integration tests for {@link UserService}.
 */
@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class UserServiceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "dummy";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditingHandler auditingHandler;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Event event;

    @BeforeEach
    public void init() {
        user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);

        when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
        auditingHandler.setDateTimeProvider(dateTimeProvider);

        categoryRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
        User userOwner = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userOwner.setFirstName("ana");
        userOwner.setLastName("maria");
        userRepository.save(userOwner);
        Address address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        Set<Category> set = new HashSet<>();
        event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, userRepository.findAll().get(0), set);
        Category category = TestsRepositoryUtil.createCategory("Ana", "description1");
        Category category1 = TestsRepositoryUtil.createCategory("MARIA", "description1");
        event.getCategories().add(category);
        category.getEvents().add(event);
        User user1 = TestsRepositoryUtil.createUser("login1", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        user1.getEvents().add(event);
        event.getUsers().add(user1);
        userRepository.save(user1);
        eventRepository.save(event);
        categoryRepository.save(category1);
    }

    @Test
    @Transactional
    public void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(user);
        Optional<User> maybeUser = userService.requestPasswordReset("invalid.login@localhost");
        assertThat(maybeUser).isNotPresent();

        maybeUser = userService.requestPasswordReset(user.getEmail());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(user.getEmail());
        assertThat(maybeUser.orElse(null).getResetDate()).isNotNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNotNull();
    }

    @Test
    @Transactional
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.requestPasswordReset(user.getLogin());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatResetKeyMustBeValid() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isNotPresent();
        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatUserCanResetPassword() {
        String oldPassword = user.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getResetDate()).isNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNull();
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatNotActivatedUsersWithNotNullActivationKeyCreatedBefore3DaysAreDeleted() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        user.setActivated(false);
        user.setActivationKey(RandomStringUtils.random(20));
        User dbUser = userRepository.saveAndFlush(user);
        dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);
        List<User> users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isNotEmpty();
        userService.removeNotActivatedUsers();
        users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isEmpty();
    }

    @Test
    @Transactional
    public void assertThatNotActivatedUsersWithNullActivationKeyCreatedBefore3DaysAreNotDeleted() {
        Instant now = Instant.now();
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(now.minus(4, ChronoUnit.DAYS)));
        user.setActivated(false);
        User dbUser = userRepository.saveAndFlush(user);
        dbUser.setCreatedDate(now.minus(4, ChronoUnit.DAYS));
        userRepository.saveAndFlush(user);
        List<User> users = userRepository.findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
        assertThat(users).isEmpty();
        userService.removeNotActivatedUsers();
        Optional<User> maybeDbUser = userRepository.findById(dbUser.getId());
        assertThat(maybeDbUser).contains(dbUser);
    }

    @Test
    @Transactional
    public void assertThatAnonymousUserIsNotGet() {
        user.setLogin(Constants.ANONYMOUS_USER);
        if (!userRepository.findOneByLogin(Constants.ANONYMOUS_USER).isPresent()) {
            userRepository.saveAndFlush(user);
        }
        final PageRequest pageable = PageRequest.of(0, (int) userRepository.count());
        final Page<UserDTO> allManagedUsers = userService.getAllManagedUsers(pageable);
        assertThat(allManagedUsers.getContent().stream()
            .noneMatch(user -> Constants.ANONYMOUS_USER.equals(user.getLogin())))
            .isTrue();
    }

    @Test
    public void assertThatFindByInterestedCategoryNameIsWorkingWithValidNameAndSomeUsers() throws NotFoundException {

        List<UserDTO> list = userService.findByInterestedCategoryName("Ana");
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatFindByInterestedCategoryNameIsWorkingWithValidNameAndNoUsers() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.findByInterestedCategoryName("MARIA"));
    }

    @Test
    public void assertThatFindByInterestedCategoryNameIsWorkingWithInvalidCategoryName() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.findByInterestedCategoryName("MARIA11"));
    }

    @Test
    public void assertThatFindByInterestedCategoryNameIsWorkingWithNullCategoryName() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.findByInterestedCategoryName(null));
    }

    @Test
    public void assertThatFindUsersByFirstAndLastNameIsWorkingWithValidArguments() throws NotFoundException, InvalidArgumentsException {
        List<UserDTO> users = userService.findUsersByFirstAndLastName("ana", "maria");
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void assertThatFindUsersByFirstAndLastNameIsWorkingWithInvalidFirstName() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.findUsersByFirstAndLastName("ana1", "maria"));
    }

    @Test
    public void assertThatFindUsersByFirstAndLastNameIsWorkingWithInvalidLastName() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.findUsersByFirstAndLastName("ana", "maria1"));
    }

    @Test
    public void assertThatFindUsersByFirstAndLastNameIsWorkingWithNullFirstName() {

        Assertions.assertThrows(InvalidArgumentsException.class, () -> userService.findUsersByFirstAndLastName(null, "maria"));
    }

    @Test
    public void assertThatFindUsersByFirstAndLastNameIsWorkingWithNullLastName() {

        Assertions.assertThrows(InvalidArgumentsException.class, () -> userService.findUsersByFirstAndLastName("ana", null));
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithValidArgument() throws NotFoundException {
        List<String> list = userService.searchByKeyword("an");
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithInvalidArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.searchByKeyword("lllll"));
    }

    @Test
    public void assertThatSearchByKeywordIsWorkingWithNullArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> userService.searchByKeyword(null));
    }
}
