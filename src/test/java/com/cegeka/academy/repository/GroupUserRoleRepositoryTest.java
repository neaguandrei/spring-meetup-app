package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.Group;
import com.cegeka.academy.domain.GroupUserRole;
import com.cegeka.academy.domain.Role;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class GroupUserRoleRepositoryTest {

    @Autowired
    private GroupUserRoleRepository groupUserRoleRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user1, user2;
    private Group group;
    private GroupUserRole groupUserRole1, groupUserRole2;
    private Role role;

    @BeforeEach
    public void init() {

        user1 = TestsRepositoryUtil.createUser("login1", "1aaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.saveAndFlush(user1);
        user2 = TestsRepositoryUtil.createUser("login2", "2ananaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.saveAndFlush(user2);
        group = TestsRepositoryUtil.createGroup("gr1", "ana are mere");
        groupRepository.save(group);
        role = TestsRepositoryUtil.createRole("admin");
        roleRepository.save(role);
        groupUserRole1 = TestsRepositoryUtil.createGroupUserRole(user1, groupRepository.findAll().get(0), roleRepository.findAll().get(0));
        groupUserRoleRepository.save(groupUserRole1);
        groupUserRole2 = TestsRepositoryUtil.createGroupUserRole(user2, groupRepository.findAll().get(0), roleRepository.findAll().get(0));
        groupUserRoleRepository.save(groupUserRole2);
    }

    @Test
    public void testSaveGroupUserRole() {

        List<GroupUserRole> list = groupUserRoleRepository.findAll();
        Optional<Group> findGroup1 = groupRepository.findById(list.get(0).getGroup().getId());
        Optional<Role> findRole1 = roleRepository.findById(list.get(0).getRole().getId());
        Optional<User> findUser1 = userRepository.findById(list.get(0).getUser().getId());
        Optional<Group> findGroup2 = groupRepository.findById(list.get(1).getGroup().getId());
        Optional<Role> findRole2 = roleRepository.findById(list.get(1).getRole().getId());
        Optional<User> findUser2 = userRepository.findById(list.get(1).getUser().getId());

        assertThat(list.size()).isEqualTo(2);
        Assert.assertTrue(findGroup1.isPresent());
        Assert.assertTrue(findRole1.isPresent());
        Assert.assertTrue(findUser1.isPresent());
        Assert.assertTrue(findGroup2.isPresent());
        Assert.assertTrue(findRole2.isPresent());
        Assert.assertTrue(findUser2.isPresent());

    }

    @Test
    public void testFindAllByGroupId() {

        Long idGroup = groupRepository.findAll().get(0).getId();
        List<GroupUserRole> list = groupUserRoleRepository.findAllByGroupId(idGroup);
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void testFindAllByGroupIdWithNullInput() {

        List<GroupUserRole> list = groupUserRoleRepository.findAllByGroupId(null);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void testFindAllByGroupIdWithInvalidInput() {

        List<GroupUserRole> list = groupUserRoleRepository.findAllByGroupId(100L);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void testFindAllByGroupIdWithNoGroupUserRole() {

        groupUserRoleRepository.deleteAll();
        List<GroupUserRole> list = groupUserRoleRepository.findAllByGroupId(1L);
        assertThat(list.size()).isEqualTo(0);
    }
}


