package ru.skqwk.elearningsystem.services;

import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;

import java.util.List;

public interface IELearningService {

     long countTeachers();
     void deleteTeacher(Teacher teacher);
     void saveTeacher(Teacher teacher);
     List<Teacher> findAllTeachersByFilter(String filterText);

     List<Course> findAllCourses();
     void deleteCourse(Course course);
     void saveCourse(Course course);

     List<Group> findAllGroups();
     void saveGroup(Group group);
     void deleteGroup(Group group);

     List<Department> findAllDepartments();

     List<Student> findAllStudents();
     void deleteStudent(Student student);
     void saveStudent(Student student);

     List<Teacher> findAllTeachers();



}
