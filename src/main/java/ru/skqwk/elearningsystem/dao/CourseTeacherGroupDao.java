package ru.skqwk.elearningsystem.dao;

import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseTeacherGroupDao extends AbstractDao<CourseTeacherGroup> {

    List<CourseTeacherGroup> findAllByCourseIsNot(Course course);

    List<CourseTeacherGroup> findAllByCourse(Course course);

    List<CourseTeacherGroup> findAllByGroupIsNot(Group group);
}
