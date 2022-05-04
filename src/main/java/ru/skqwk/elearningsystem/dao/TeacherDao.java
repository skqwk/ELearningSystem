package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Teacher;

import java.util.List;

public interface TeacherDao extends AbstractDao<Teacher> {

    @Query("select t from Teacher t " +
            "where lower(t.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(t.surname) like lower(concat('%', :searchTerm, '%'))")
    List<Teacher> search(@Param("searchTerm") String searchTerm);
}
