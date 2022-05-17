package ru.skqwk.elearningsystem.services;

import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.EducationalMaterial;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.StudyStatus;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;
import ru.skqwk.elearningsystem.model.enumeration.Status;

import java.util.Collection;
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

    List<CourseTeacherGroup> findAllCourseTeacherGroups();
    CourseTeacherGroup saveCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup);
    void deleteCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup);

     List<Department> findAllDepartments();

     List<Student> findAllStudents();
     void deleteStudent(Student student);
     void saveStudent(Student student);

     List<Teacher> findAllTeachers();


    void deleteDepartment(Department department);

     void saveDepartment(Department department);

    List<Student> findAllStudentsWithoutGroup();

     Collection<CourseTeacherGroup> findAcademicPlanForGroup(Long id);

    List<Course> findAllCoursesWithoutGroup(Group group);

    List<Teacher> findAllTeachersByDepartment(Department department);

    Teacher findTeacherByUserId(Long id);

    Student findStudentByUserId(Long id);

    List<CourseTeacherGroup> findAllCourseTeacherGroupsByTeacher(Teacher teacher);

    List<CourseTeacherGroup> findAllCourseTeacherGroupsByGroup(Group group);

    void saveStudyStatus(StudyStatus status);

    List<EducationalMaterial> findAllMaterialsByCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup);

    List<String> getStudentStudyStatusesString(Student student, List<EducationalMaterial> materials);
    List<StudyStatus> getStudentStudyStatuses(Student student, List<EducationalMaterial> materials);

    Status getStudentMaterialStatus(Student student, EducationalMaterial material);

    void saveMaterial(EducationalMaterial material);
}
