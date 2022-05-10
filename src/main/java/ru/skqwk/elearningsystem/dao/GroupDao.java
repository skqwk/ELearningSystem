package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Group;
import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    List<Group> findAllByCoursesNotContains(Course course);

    List<Group> findAllByCoursesContains(Course course);
}

