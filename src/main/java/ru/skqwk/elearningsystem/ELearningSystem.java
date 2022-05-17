package ru.skqwk.elearningsystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.skqwk.elearningsystem.dao.CourseDao;
import ru.skqwk.elearningsystem.dao.CourseTeacherGroupDao;
import ru.skqwk.elearningsystem.dao.DepartmentDao;
import ru.skqwk.elearningsystem.dao.EducationalMaterialDao;
import ru.skqwk.elearningsystem.dao.GroupDao;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.dao.StudyStatusDao;
import ru.skqwk.elearningsystem.dao.TeacherDao;
import ru.skqwk.elearningsystem.dao.UserDao;
import ru.skqwk.elearningsystem.model.Course;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.EducationalMaterial;
import ru.skqwk.elearningsystem.model.Group;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.StudyStatus;
import ru.skqwk.elearningsystem.model.Teacher;
import ru.skqwk.elearningsystem.model.dto.CourseTeacherGroup;
import ru.skqwk.elearningsystem.model.enumeration.MaterialType;
import ru.skqwk.elearningsystem.model.enumeration.Status;

import java.util.List;

@SpringBootApplication
@Slf4j
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
			CourseTeacherGroupDao courseTeacherGroupDao,
			UserDao userDao,
			StudyStatusDao studyStatusDao,
			EducationalMaterialDao educationalMaterialDao

	) {
		return args -> {
			log.info("Insert entities");
			Department mathDepart = Department.builder()
					.name("Кафедра математики")
					.build();

			departmentDao.save(mathDepart);
			departmentDao.save(Department.builder()
					.name("Кафедра иностранных языков")
					.build());
			departmentDao.save(Department.builder()
					.name("Кафедра естественных наук")
					.build());

			Course algebra = Course.builder()
					.name("Алгебра")
					.department(mathDepart)
					.build();

			Course geometry = Course.builder()
					.name("Геометрия")
					.department(mathDepart)
					.build();

			Course math = Course.builder()
					.name("Математика")
					.department(mathDepart)
					.build();

			courseDao.save(geometry);
			courseDao.save(algebra);
			courseDao.save(math);




			Group group1 = Group.builder()
					.literal("A")
					.number(10)
					.build();

			Student student1 = Student.builder()
					.name("Петр")
					.surname("Петров")
					.patronymic("Петрович")
					.user(userDao.findUserByLogin("student"))
					.group(group1)
					.build();

			Student student2 = Student.builder()
					.name("Сидор")
					.surname("Сидоров")
					.patronymic("Сидорович")
					.group(group1)
					.build();

			group1.setStudents(List.of(student1, student2));

			Group group2 = Group.builder()
					.literal("B")
					.number(9)
					.build();

			Student student3 = Student.builder()
					.name("Иван")
					.surname("Иванов")
					.patronymic("Иванович")
					.group(group2)
					.build();

			Student student4 = Student.builder()
					.name("Алла")
					.surname("Максимова")
					.patronymic("Анатольевна")
					.group(group2)
					.build();

			group2.setStudents(List.of(student3, student4));


			groupDao.save(group1);
			groupDao.save(group2);

			studentDao.save(student1);
			studentDao.save(student2);
			studentDao.save(student3);
			studentDao.save(student4);

			teacherDao.save(Teacher.builder()
							.name("Иван")
							.surname("Петров")
							.patronymic("Сидорович")
							.workExperience(10)
							.department(mathDepart)
					.build());

			teacherDao.save(Teacher.builder()
							.name("Мария")
							.surname("Иванова")
							.patronymic("Петровна")
							.workExperience(6)
							.department(mathDepart)
					.build());

			Teacher mathTeacher = Teacher.builder()
					.name("Александр")
					.surname("Сидоров")
					.patronymic("Петрович")
					.user(userDao.findUserByLogin("teacher"))
					.workExperience(5)
					.department(mathDepart)
					.build();

			teacherDao.save(mathTeacher);

			courseTeacherGroupDao.save(CourseTeacherGroup.builder()
					.course(math)
					.teacher(mathTeacher)
					.group(group1)
					.build());

			courseTeacherGroupDao.save(CourseTeacherGroup.builder()
					.course(geometry)
					.teacher(mathTeacher)
					.group(group1)
					.build());

			courseTeacherGroupDao.save(CourseTeacherGroup.builder()
					.course(math)
					.teacher(mathTeacher)
					.group(group2)
					.build());

			courseTeacherGroupDao.save(CourseTeacherGroup.builder()
					.course(geometry)
					.teacher(mathTeacher)
					.group(group2)
					.build());

			CourseTeacherGroup ctg = CourseTeacherGroup.builder()
					.course(algebra)
					.teacher(mathTeacher)
					.group(group1)
					.build();

			courseTeacherGroupDao.save(ctg);

			EducationalMaterial lesson = EducationalMaterial.builder()
					.title("Сложение дробей")
					.type(MaterialType.LESSON)
					.content("Чтобы складывать дроби нужно")
					.courseTeacherGroup(ctg)
					.build();

			educationalMaterialDao.save(lesson);

			StudyStatus status = StudyStatus.builder()
					.status(Status.LEARNED)
					.material(lesson)
					.student(student3)
					.build();



			studyStatusDao.save(status);

		};
	}

}
