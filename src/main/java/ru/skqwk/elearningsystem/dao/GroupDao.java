package ru.skqwk.elearningsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skqwk.elearningsystem.model.Group;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {
}

