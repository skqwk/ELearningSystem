package ru.skqwk.elearningsystem.services;

import org.springframework.stereotype.Service;
import ru.skqwk.elearningsystem.dao.CourseDao;
import ru.skqwk.elearningsystem.dao.CourseTeacherGroupDao;
import ru.skqwk.elearningsystem.dao.DepartmentDao;
import ru.skqwk.elearningsystem.dao.GroupDao;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.dao.TeacherDao;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ELearningService implements IELearningService{
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final DepartmentDao departmentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;
    private final CourseTeacherGroupDao courseTeacherGroupDao;

    public ELearningService(TeacherDao teacherDao,
                            StudentDao studentDao,
                            DepartmentDao departmentDao,
                            GroupDao groupDao,
                            CourseDao courseDao,
                            CourseTeacherGroupDao courseTeacherGroupDao) {
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.departmentDao = departmentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.courseTeacherGroupDao = courseTeacherGroupDao;
    }

    @Override
    public List<Teacher> findAllTeachersByFilter(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return teacherDao.findAll();
        } else {
            return teacherDao.search(filterText);
        }
    }

    @Override
    public long countTeachers() {
        return teacherDao.count();
    }

    @Override
    public void deleteTeacher(Teacher teacher) {
        teacherDao.delete(teacher);
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        if (teacher == null) {
            System.err.println("Teacher is null");
            return;
        }
        teacherDao.save(teacher);
    }

    @Override
    public List<Department> findAllDepartments() {
        return departmentDao.findAll();
    }

    @Override
    public List<Course> findAllCourses() {
        return courseDao.findAll();
    }

    @Override
    public void deleteCourse(Course course) {
        courseDao.delete(course);
    }

    @Override
    public void saveCourse(Course course) {
        courseDao.save(course);

    }

    @Override
    public List<Group> findAllGroups() {
        return groupDao.findAll();}

    @Override
    public void saveGroup(Group group) {
        groupDao.save(group);
    }

    @Override
    public void deleteGroup(Group group) {
        groupDao.delete(group);

    }

    @Override
    public List<CourseTeacherGroup> findAllCourseTeacherGroups() {
        return courseTeacherGroupDao.findAll();
    }

    @Override
    public CourseTeacherGroup saveCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup) {
        return courseTeacherGroupDao.save(courseTeacherGroup);
    }

    @Override
    public void deleteCourseTeacherGroup(CourseTeacherGroup courseTeacherGroup) {
        courseTeacherGroupDao.delete(courseTeacherGroup);

    }

    @Override
    public List<Student> findAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public void deleteStudent(Student student) {
        studentDao.delete(student);
    }

    @Override
    public void saveStudent(Student student) {
        studentDao.save(student);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherDao.findAll();
    }

    @Override
    public void deleteDepartment(Department department) {
        departmentDao.delete(department);
    }

    @Override
    public void saveDepartment(Department department) {
        departmentDao.save(department);
    }

    @Override
    public List<Student> findAllStudentsWithoutGroup() {
        return studentDao.findAllByGroupIsNull();
    }

    @Override
    public Collection<CourseTeacherGroup> findAcademicPlanForGroup(Long id) {
//        List<Teacher> teachers = teacherDao.findAllByGroupId()
        return null;
    }

    @Override
    public List<Group> findAllGroupsWithoutCourse(Course course) {
        if (course.getId() == null) return findAllGroups();
        return groupDao.findAllByCoursesNotContains(course);
//        return courseTeacherGroupDao.findAllByCourseIsNot(course).stream()
//                .map(CourseTeacherGroup::getGroup)
//                .collect(Collectors.toList());
    }

    @Override
    public List<Group> findAllGroupsWithCourse(Course course) {
        if (course.getId() == null) return new ArrayList<>();
        return groupDao.findAllByCoursesContains(course);
//        return courseTeacherGroupDao.findAllByCourse(course).stream()
//                .map(CourseTeacherGroup::getGroup)
//                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findAllCoursesWithoutGroup(Group group) {
        List<Course> courses = courseDao.findAll();
        List<Course> selectedCourses = group.getCourseTeacherGroups()
                .stream()
                .map(CourseTeacherGroup::getCourse)
                .collect(Collectors.toList());


        selectedCourses.forEach(c -> System.out.println(c.getName()));

        for (Course course : courses) {

            System.out.println("Selected courses contains " + course.getName() + " = "+ selectedCourses.contains(course));
        }


        List<Course> uniqueCourses = courses.stream()
                .filter(c -> !selectedCourses.contains(c))
                .collect(Collectors.toList());

        return uniqueCourses;
    }

    @Override
    public List<Teacher> findAllTeachersByDepartment(Department department) {
        return teacherDao.findAllByDepartment(department);
    }

}
