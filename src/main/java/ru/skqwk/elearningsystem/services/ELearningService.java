package ru.skqwk.elearningsystem.services;

import org.springframework.stereotype.Service;
import ru.skqwk.elearningsystem.dao.DepartmentDao;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.dao.TeacherDao;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;
import java.util.List;


@Service
public class ELearningService {
    private final TeacherDao teacherDao;
    private final StudentDao studentDao;
    private final DepartmentDao departmentDao;

    public ELearningService(TeacherDao teacherDao,
                            StudentDao studentDao,
                            DepartmentDao departmentDao) {
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.departmentDao = departmentDao;
    }

    public List<Teacher> findAllTeachersByFilter(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return teacherDao.findAll();
        } else {
            return teacherDao.search(filterText);
        }
    }

    public long countTeachers() {
        return teacherDao.count();
    }

    public void deleteTeacher(Teacher teacher) {
        teacherDao.delete(teacher);
    }

    public void saveTeacher(Teacher teacher) {
        if (teacher == null) {
            System.err.println("Teacher is null");
            return;
        }

        teacherDao.save(teacher);
    }

    public List<Department> findAllDepartments() {
        return departmentDao.findAll();
    }

    public List<Student> findAllStudents() {
        return studentDao.findAll();
    }

    public List<Teacher> findAllTeachers() {
        return teacherDao.findAll();
    }

}
