package ru.skqwk.elearningsystem.services;

import org.springframework.stereotype.Service;
import ru.skqwk.elearningsystem.dao.CourseDao;
import ru.skqwk.elearningsystem.dao.DepartmentDao;
import ru.skqwk.elearningsystem.dao.GroupDao;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.dao.TeacherDao;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;
import java.util.List;


@Service
public class ELearningService implements IELearningService{
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final DepartmentDao departmentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;

    public ELearningService(TeacherDao teacherDao,
                            StudentDao studentDao,
                            DepartmentDao departmentDao,
                            GroupDao groupDao,
                            CourseDao courseDao) {
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.departmentDao = departmentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
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
    public List<Course> findAllCourses() {return courseDao.findAll();}

    @Override
    public void deleteCourse(Course course) {
        courseDao.delete(course);
    }

    @Override
    public void saveCourse(Course course) {
        courseDao.save(course);

    }

    @Override
    public List<Group> findAllGroups() {return groupDao.findAll();}

    @Override
    public void saveGroup(Group group) {
        groupDao.save(group);
    }

    @Override
    public void deleteGroup(Group group) {
        groupDao.delete(group);

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

}
