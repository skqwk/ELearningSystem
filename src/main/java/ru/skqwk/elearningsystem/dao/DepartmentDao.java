package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Department;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Long> {
}
