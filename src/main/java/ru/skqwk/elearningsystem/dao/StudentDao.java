package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Student;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
    List<Student> findAllByGroupIsNull();

}
