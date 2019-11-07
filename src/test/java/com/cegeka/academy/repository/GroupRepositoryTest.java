package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.Group;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    private Group group;

    @BeforeEach
    public void init() {

        groupRepository.deleteAll();
        group = TestsRepositoryUtil.createGroup("ana", "ana are mere");
        groupRepository.save(group);
    }

    @Test
    public void assertThatSaveGroupIsWorking() {

        assertThat(groupRepository.findAll().size()).isEqualTo(1);
        assertThat(groupRepository.findAll().get(0).getDescription()).isEqualTo(group.getDescription());
        assertThat(groupRepository.findAll().get(0).getName()).isEqualTo(group.getName());

    }

    @Test
    public void assertThatFindByNameIsWorkingWithValidArgument() {

        Group findGroup = groupRepository.findByName("ana");
        assertThat(findGroup).isEqualTo(groupRepository.findAll().get(0));
    }

    @Test
    public void assertThatFindByNameIsWorkingWithInvalidArgument() {

        Group findGroup = groupRepository.findByName("ana1");
        assertThat(findGroup).isEqualTo(null);
    }

    @Test
    public void assertThatFindByNameIsWorkingWithNullArgument() {

        Group findGroup = groupRepository.findByName(null);
        assertThat(findGroup).isEqualTo(null);
    }
}
