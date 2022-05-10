package ru.skqwk.elearningsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

import java.util.List;

@SpringBootApplication
public class ELearningSystem {

	public static void main(String[] args) {
		SpringApplication.run(ELearningSystem.class, args);
	}

	@Bean
	CommandLineRunner initRepo(
			GroupDao groupDao,
			CourseDao courseDao,
			StudentDao studentDao,
			TeacherDao teacherDao,
			DepartmentDao departmentDao,
			CourseTeacherGroupDao courseTeacherGroupDao

	) {
		return args -> {

			Department math = Department.builder()
					.name("Кафедра математики")
					.build();

			departmentDao.save(math);
			departmentDao.save(Department.builder()
					.name("Кафедра иностранных языков")
					.build());
			departmentDao.save(Department.builder()
					.name("Кафедра естественных наук")
					.build());

			Course mathCourse = Course.builder()
					.name("Алгебра. Начало математического анализа")
					.department(math)
					.build();

			courseDao.save(mathCourse);




			Group group = Group.builder()
					.literal("A")
					.number(10)
					.build();



			Student student1 = Student.builder()
					.name("Петр")
					.surname("Петров")
					.patronymic("Петрович")
					.group(group)
					.build();

			Student student2 = Student.builder()
					.name("Сидор")
					.surname("Сидоров")
					.patronymic("Сидорович")
					.group(group)
					.build();

			Student student3 = Student.builder()
					.name("Иван")
					.surname("Иванов")
					.patronymic("Иванович")
					.build();

			group.setStudents(List.of(student1, student2));
			group.setCourses(List.of(mathCourse));
			mathCourse.setGroups(List.of(group));
			groupDao.save(group);
			courseDao.save(mathCourse);


//			System.out.println(groupDao.findById(1L).get().getCourses());
//			Course course = courseDao.getById(1L);
//			System.out.println(course.getGroups());

			studentDao.save(student1);
			studentDao.save(student2);
			studentDao.save(student3);

			teacherDao.save(Teacher.builder()
							.name("Иван")
							.surname("Петров")
							.patronymic("Сидорович")
							.workExperience(10)
							.department(math)
					.build());

			teacherDao.save(Teacher.builder()
							.name("Мария")
							.surname("Иванова")
							.patronymic("Петровна")
							.workExperience(6)
							.department(math)
					.build());

			Teacher mathTeacher = Teacher.builder()
					.name("Александр")
					.surname("Сидоров")
					.patronymic("Петрович")
					.workExperience(5)
					.department(math)
					.build();

			teacherDao.save(mathTeacher);
			CourseTeacherGroup courseTeacherGroup = CourseTeacherGroup.builder()
					.course(mathCourse)
					.teacher(mathTeacher)
					.group(group)
					.build();

			courseTeacherGroupDao.save(courseTeacherGroup);
		};
	}

}
