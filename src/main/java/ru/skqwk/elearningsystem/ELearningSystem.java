package ru.skqwk.elearningsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.skqwk.elearningsystem.dao.DepartmentDao;
import ru.skqwk.elearningsystem.dao.StudentDao;
import ru.skqwk.elearningsystem.dao.TeacherDao;
import ru.skqwk.elearningsystem.model.Department;
import ru.skqwk.elearningsystem.model.Student;
import ru.skqwk.elearningsystem.model.Teacher;

@SpringBootApplication
public class ELearningSystem {

	public static void main(String[] args) {
		SpringApplication.run(ELearningSystem.class, args);
	}

	@Bean
	CommandLineRunner initRepo(
			StudentDao studentDao,
			TeacherDao teacherDao,
			DepartmentDao departmentDao
	) {
		return args -> {

			Department department = Department.builder()
					.name("Кафедра математики")
					.build();

			departmentDao.save(department);

			studentDao.save(Student.builder()
					.name("Иван")
					.surname("Иванов")
					.patronymic("Иванович")
					.build());

			studentDao.save(Student.builder()
					.name("Петр")
					.surname("Петров")
					.patronymic("Петрович")
					.build());

			studentDao.save(Student.builder()
					.name("Сидор")
					.surname("Сидоров")
					.patronymic("Сидорович")
					.build());


			teacherDao.save(Teacher.builder()
							.name("Иван")
							.surname("Петров")
							.patronymic("Сидорович")
							.workExperience(10)
							.department(department)
					.build());

			teacherDao.save(Teacher.builder()
							.name("Мария")
							.surname("Иванова")
							.patronymic("Петровна")
							.workExperience(6)
							.department(department)
					.build());

			teacherDao.save(Teacher.builder()
							.name("Александр")
							.surname("Сидоров")
							.patronymic("Петрович")
							.workExperience(5)
							.department(department)
					.build());
		};
	}

}
