package ru.skqwk.elearningsystem.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skqwk.elearningsystem.dao.UserDao;
import ru.skqwk.elearningsystem.model.User;
import ru.skqwk.elearningsystem.model.enumeration.UserRole;

@Service
@Slf4j
public class UserDaoInit implements CommandLineRunner {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    public UserDaoInit(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Insert users");
        User admin = User.builder()
                .login("admin")
                .password(passwordEncoder.encode("userpass"))
                .role(UserRole.ADMIN)
                .build();

        User student = User.builder()
                .login("student")
                .password(passwordEncoder.encode("userpass"))
                .role(UserRole.STUDENT)
                .build();

        User teacher = User.builder()
                .login("teacher")
                .password(passwordEncoder.encode("userpass"))
                .role(UserRole.TEACHER)
                .build();

        userDao.save(admin);
        userDao.save(student);
        userDao.save(teacher);
    }
}